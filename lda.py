#!/usr/bin/env python
# vim:ts=4:sts=4:sw=4:et:wrap:ai:fileencoding=utf-8

import json
import math
import numpy
import inspect
import warnings
from collections import defaultdict
from gensim import corpora, models, similarities
from gensim.models import hdpmodel, ldamodel
from itertools import izip
from engine.tools.contentbased.stopwords import WORDS_STOPLIST, IGNORE_WORDS, PREPOSITIONS_LIST
from engine.common.normalize import normalize_string
from engine.tools.contentbased.create_model import get_terms
from engine.common.filters import filter_and_classify_input
from sklearn import metrics
from gensim.models.tfidfmodel import TfidfModel
from scipy.sparse import csr_matrix
from scipy.sparse import lil_matrix
import scipy.sparse as sps

documents = ["Sistemas sobre bananas são bananas",
"Bananas são nutritivas",
"Sistemas construídos por pandas e bambus.",
"Adote uma banana hoje!",
"Sistemas e grafos sobre tudo!"]

filename = "inputs_sepha_ok"

class AlgorithmsWrapper:

  def __init__(self, texts_per_cat, use_skip = False, algorithm='firstname', use_stemming=True, use_description=False):
    self.use_skip = use_skip
    self.algorithm = algorithm
    self.texts_per_cat = texts_per_cat
    self.use_stemming = use_stemming
    self.use_description = use_description
    print "using algorithm: "+self.algorithm
    print "use skipping: "+str(self.use_skip)
    print "use description: "+str(self.use_description)
    print "use stemming: "+str(self.use_stemming)
    self.macro_avg_sum = 0.0
    self.micro_avg_sum = 0.0
    self.cont_samples = 0.0
    self.total_corpus = 0.0
    self.skips = 0
    self.final_results = []

  def run(self):
    for category_id, objs in texts_per_cat.iteritems():
      print "\n\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n"
      print "Processing category: "+str(category_id)
      if category_id.isdigit():
        print "Processing "+str(len(objs))+" objects"
        if algorithm=='firstname':
          self.first_name(objs)
        else
          self.algorithm_lda(category_id, objs)
      else:
        print "category_id is not a digit, ignoring"
    self.print_results()

  def algorithm_lda(self, category_id, objs):
    numTopics = self.calculate_k(objs)
    print "Using k = "+str(numTopics)

    texts = []
    for obj in objs:
      texts.append(self.get_categorizedproduct_content(obj))

    dictionary = corpora.Dictionary(texts)
    corpus = [dictionary.doc2bow(text) for text in texts] # bag of words

    print "print out the topics for LDA"
    lda = ldamodel.LdaModel(corpus, id2word=dictionary, num_topics=numTopics, passes=100)
    topics = []
    for i in range(0, lda.num_topics):
      print "Topic #" + str(i) + ": "+lda.print_topic(i)+"\n"
      topics.append(i)
    print

    print "print out the documents and which is the most probable topics for each doc"
    corpus_lda = lda[corpus]
    results = []
    labels = []
    for probabilities, obj in izip(corpus_lda, objs):
      max_prop = max(probabilities, key=lambda item:item[1])[0]
      labels.append(max_prop)
      results.append(str(max_prop)+" # "+obj['name'].encode('utf8'))
    results.sort()
    for r in results:
      print r

    if numTopics > 1:
      self.calculate_metrics(category_id, corpus, dictionary, labels)
    else:
      print "number of clusters equals or lower than 1, ignoring metric"

    # print "A"
    # # print out the topics for LSA
    # lsi = models.LsiModel(corpus, id2word=dictionary, num_topics=2)
    # corpus_lsi = lsi[corpus]
    # print str(corpus_lsi)+"\n"
    # print str(corpus)+"\n"
    # for l,t in izip(corpus_lsi,corpus):
    #   print l,"#",t
    # print
    # for top in lsi.print_topics(2):
    #   print top

  def first_name(self, category_id, objs):
    first_names = set([item['name'].split()[0] for item in objs])
    numTopics = len(first_names)
    print "Using k = "+str(numTopics)

    texts = []
    for obj in objs:
      texts.append(self.get_categorizedproduct_content(obj))

    dictionary = corpora.Dictionary(texts)
    corpus = [dictionary.doc2bow(text) for text in texts] # bag of words

    labels = []
    for item in texts:
      labels.append(item[0])

    topics = set(labels)

    print "print out the clusters"
    for i, t in enumerate(topics):
      print "Topic #" + str(i) + ": "+str(t)+"\n"
    print

    print "print out the documents and which is the most probable cluster for each doc"
    results = []
    for label, obj in izip(labels, objs):
      results.append(str(label)+" # "+obj['name'].encode('utf8'))
    results.sort()
    for r in results:
      print r

    if numTopics > 1:
      self.calculate_metrics(category_id, corpus, dictionary, labels)
    else:
      print "number of clusters equals or lower than 1, ignoring metric"

  def get_categorizedproduct_content(self, event, use_description=self.use_description, use_preposition=True, use_stemming=self.use_stemming):
    # We check all fields for None if they are None we put an empty string instead
    name = (event.get("name") or "")
    description = ""
    if use_description:
      if 'info' in event and 'description' in event['info']:
        description = event['info']['description']
    tags = event.get("tags", []) or []
    event['tags'] = filter(None, event['tags'])
    return get_terms(name, use_preposition=use_preposition, use_stemming=use_stemming) + \
        get_terms(description, use_preposition=use_preposition, use_stemming=use_stemming) + \
        get_terms(tags, use_preposition=use_preposition, use_stemming=use_stemming)

  def calculate_k(self, objs):
    first_names = set([item['name'].split()[0] for item in objs])
    # result = int(round(math.sqrt(len(first_names))))
    result = len(first_names)
    if result > 100:
      result = 100
    if result >= len(objs):
      result = len(objs)-1
    if result == 0:
      result = 1
    return result

  # def print_topics(lda, vocab, n=10):
  #     """ Print the top words for each topic. """
  #     topics = lda.show_topics(topics=-1, topn=n, formatted=False)
  #     for ti, topic in enumerate(topics):
  #         print 'topic %d: %s' % (ti, ' '.join('%s/%.2f' % (t[1], t[0]) for t in topic))

  def calculate_metrics(self, category_id, corpus, dictionary, labels):
    tfidf = TfidfModel(corpus)
    features = lil_matrix(((len(corpus), len(dictionary.keys()))))
    for corpus_id, item in enumerate(corpus):
      for (word_id, tfidf_value) in tfidf[item]:
        features[corpus_id, word_id] = tfidf_value
    # X = [[1, 1, 1, 1], [1, 1, 1, 1], [1, 1, 1, 1], [1, 1, 1, 1]]
    # X = numpy.array(features)
    X = features
    # labels = [1, 2, 3, 4]
    labels = numpy.array(labels)
    try:
      avg_silhouette = numpy.mean(metrics.silhouette_score(X, labels, metric='euclidean',sample_size=500))
    except ValueError as e:
      if self.use_skip:
        print "skipping"
        self.skips += 1
        continue
      else:
        avg_silhouette = 0
    msg = str(category_id)+": "+str(avg_silhouette)
    print msg
    self.final_results.append(msg)
    self.macro_avg_sum += avg_silhouette
    self.micro_avg_sum += avg_silhouette*float(len(labels))
    self.cont_samples += 1.0
    self.total_corpus += float(len(labels))

  def print_results(self):
    print "\nFINAL RESULTS:"
    if self.total_corpus > 0:
      print "micro avg: "+str(self.micro_avg_sum/self.total_corpus)
    if self.cont_samples > 0:
      print "macro avg: "+str(self.macro_avg_sum/self.cont_samples)
    print "skipped: "+str(self.skips)
    print
    print
    self.final_results.sort()
    for r in self.final_results:
      print r

def file_generator(filename):
  for line in open("data-clusterization/"+filename):
    yield line

if (__name__ == '__main__'):
  print "read inputs, normalize and tokenize"
  # warnings.simplefilter("error")
  texts_per_cat = defaultdict(list)
  for (domain, input_type, obj) in filter_and_classify_input(file_generator(filename), convert_to_version=None):
    texts_per_cat[obj.get('chaordicCategoryBidId', 'None')].append(obj)
  a = AlgorithmsWrapper(texts_per_cat, algorithm='firstname')
  a.run()
