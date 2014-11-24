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

documents = ["Sistemas sobre bananas são bananas",
"Bananas são nutritivas",
"Sistemas construídos por pandas e bambus.",
"Adote uma banana hoje!",
"Sistemas e grafos sobre tudo!"]

# primeiro, bag of words como feature (dps tfidf)
# implementar first_term e comparar algoritmos

def file_generator():
  for line in open("data-clusterization/inputs_sepha_ok"):
    yield line

def get_categorizedproduct_content(event, use_description=False, use_preposition=True, use_stemming=True):
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

def calculate_k(objs):
  first_names = [item['name'].split()[0] for item in objs]
  # result = int(round(math.sqrt(len(first_names))))
  result = len(first_names)
  if result > 100:
    result = 100
  if result >= len(objs):
    result = len(objs)-1
  if result == 0:
    result = 1
  return result

def algorithm_lda(texts_per_cat):
  cont = 0
  for category_id, objs in texts_per_cat.iteritems():
    print "\n\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n"
    print "Processing category: "+str(category_id)
    if category_id.isdigit():
      numTopics = calculate_k(objs)
      print "Using k = "+str(numTopics)
      print "Processing "+str(len(objs))+" objects"

      texts = []
      for obj in objs:
        texts.append(get_categorizedproduct_content(obj))

      dictionary = corpora.Dictionary(texts)
      corpus = [dictionary.doc2bow(text) for text in texts] # bag of words

      tfidf = TfidfModel(corpus)
      features = [[0 for item in range(len(dictionary.keys()))] for row in range(len(corpus))]
      for corpus_id, item in enumerate(corpus):
        for (word_id, tfidf_value) in tfidf[item]:
          features[corpus_id][word_id] = tfidf_value

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
        # X = [[1, 1, 1, 1], [1, 1, 1, 1], [1, 1, 1, 1], [1, 1, 1, 1]]
        X = numpy.array(features)
        # labels = [1, 2, 3, 4]
        labels = numpy.array(labels)
        # print str(X)
        # print str(labels)
        test = metrics.silhouette_samples(X, labels, metric='euclidean')
        print "result by sample: "+str(test)
        test2 = numpy.mean(metrics.silhouette_score(X, labels, metric='euclidean'))
        print "result avg: "+str(test2)
      else:
        "number of clusters equals or lower than 1, ignoring metric"

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
    else:
      pass # subsub = ""

print "read inputs, normalize and tokenize"
# warnings.simplefilter("error")
texts_per_cat = defaultdict(list)
for (domain, input_type, obj) in filter_and_classify_input(file_generator(), convert_to_version=None):
  texts_per_cat[obj.get('chaordicCategoryBidId', 'None')].append(obj)
algorithm_lda(texts_per_cat)
