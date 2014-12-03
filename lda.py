#!/usr/bin/env python
# vim:ts=4:sts=4:sw=4:et:wrap:ai:fileencoding=utf-8

import json
import math
import numpy
import time
import inspect
import warnings
import re
import collections
import os
os.environ['MPLCONFIGDIR'] = '/tmp/sklearn'
from operator import itemgetter
from collections import defaultdict
from gensim import corpora, models, similarities
from gensim.models import hdpmodel, ldamodel
from itertools import izip
from engine.tools.contentbased.stopwords import WORDS_STOPLIST, IGNORE_WORDS, PREPOSITIONS_LIST_SHORT, GENERIC_WORDS
from engine.common.normalize import normalize_string
from engine.tools.contentbased.create_model import get_terms
from engine.common.filters import filter_and_classify_input
from sklearn import metrics
from gensim.models.tfidfmodel import TfidfModel
from scipy.sparse import csr_matrix
from scipy.sparse import lil_matrix

from scipy.odr import models
from sklearn import metrics
import unittest
import os
import os.path
import tempfile
 
import numpy
from matplotlib.pyplot import plot, show
from sklearn.cluster import KMeans
from gensim.matutils import corpus2dense
import gensim
import logging
 
from gensim.corpora import mmcorpus, Dictionary
from gensim.models import lsimodel, ldamodel, tfidfmodel, rpmodel, logentropy_model, TfidfModel, LsiModel
from gensim import matutils,corpora
 
from scipy.cluster.vq import kmeans,vq

import string
import collections
 
from nltk import word_tokenize
from nltk.stem import PorterStemmer
from nltk.corpus import stopwords
from sklearn.cluster import KMeans
from sklearn.cluster import *
from sklearn.feature_extraction.text import TfidfVectorizer
from pprint import pprint

documents = ["Sistemas sobre bananas são bananas",
"Bananas são nutritivas",
"Sistemas construídos por pandas e bambus.",
"Adote uma banana hoje!",
"Sistemas e grafos sobre tudo!"]

filename = "inputs_goldstandard"
# filename = "short_sepha"
# filename = "inputs_fastshop-wcs_ok" # "inputs_staples_no_description"

def get_categorizedproduct_content(event, use_stemming=True, use_preposition=True, remove_parentheses=True, remove_hifen=False, remove_alone_numbers=True, extra_name_weight=False):
  # We check all fields for None if they are None we put an empty string instead
  name = (event.get("name") or "")
  description = ""
  if 'info' in event and 'description' in event['info'] and event['info']['description']:
    description = event['info']['description']
  name_terms = get_terms(name, use_preposition=use_preposition, use_stemming=use_stemming, remove_numbers=False, remove_parentheses=remove_parentheses, remove_hifen=remove_hifen, use_name_blacklist=True, remove_alone_numbers=remove_alone_numbers)
  result = name_terms + name_terms
  if name_terms:
    result += name_terms[0:3] + [name_terms[0]]
  if extra_name_weight:
    result += name_terms[0:3] + [name_terms[0]]
  else:
    result += get_terms(description, use_preposition=use_preposition, use_stemming=use_stemming, remove_numbers=True, remove_parentheses=remove_parentheses, remove_hifen=remove_hifen, use_name_blacklist=False, remove_alone_numbers=remove_alone_numbers)
  return result

class AlgorithmsWrapper:

  def __init__(self, texts_per_cat, algorithm='firstname', use_stemming=True):
    self.algorithm = algorithm
    self.texts_per_cat = texts_per_cat
    self.use_stemming = use_stemming
    print "using algorithm: "+self.algorithm
    print "use stemming: "+str(self.use_stemming)
    self.macro_avgs = []
    self.macro_stdevs = []
    self.micro_avgs = []
    self.total_corpus = 0.0
    self.skips = 0
    self.final_results = []
    self.upper_limit = 4
    self.homogeneity_scores = []
    self.completeness_scores = []
    self.v_measure_scores = []
    self.adjusted_rand_scores = []
    self.adjusted_mutual_info_scores = []
    self.cont_k_matches = 0
    self.cont_k_matches_max_wins = 0

  def run(self):
    start_time = time.time()
    for category_id, objs in texts_per_cat.iteritems():
      print "\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++"
      print "Processing category: "+str(category_id)
      if len(objs) >= self.upper_limit:
        goldstandards = None
        if 'goldstandard' in objs[0]:
          goldstandards = []
          for obj in objs:
            goldstandards.append(obj['goldstandard'])
        if category_id.isdigit():
          print "Processing "+str(len(objs))+" objects"
          if self.algorithm=='firstname': # good
            self.first_name(category_id, objs, goldstandards)
          elif self.algorithm=='lda': #  below average
            self.algorithm_lda(category_id, objs, goldstandards)
          elif self.algorithm=='lsi': # terrible
            self.algorithm_lsi(category_id, objs, goldstandards)
          else:
            self.run_sklearn_algorithm(category_id, objs, goldstandards)
        else:
          print "category_id is not a digit, ignoring"
      else:
        print "to few objects, ignoring"
    print
    print "using algorithm: "+self.algorithm
    print "use stemming: "+str(self.use_stemming)
    # self.print_results()
    elapsed_time = time.time() - start_time
    print "\nelapsed time: "+str(elapsed_time)
    print "cont_k_matches: "+str(self.cont_k_matches)
    print "cont_k_matches_max_wins: "+str(self.cont_k_matches_max_wins)

  # OLD TESTS

  def first_name(self, category_id, objs, goldstandards):
    numTopics = self.calculate_k_using_firstnames(objs)
    print "Using k = "+str(numTopics)

    texts = []
    for obj in objs:
      texts.append(get_categorizedproduct_content(obj))

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
      self.calculate_metrics(category_id, objs, labels, goldstandards)
    else:
      print "number of clusters equals or lower than 1, ignoring metric"

  def algorithm_lda(self, category_id, objs, goldstandards):
    numTopics = self.calculate_k_using_firstnames(objs)
    print "Using k = "+str(numTopics)

    texts = []
    for obj in objs:
      texts.append(get_categorizedproduct_content(obj))

    dictionary = corpora.Dictionary(texts)
    corpus = [dictionary.doc2bow(text) for text in texts] # bag of words

    print "print out the topics for LDA"
    lda = ldamodel.LdaModel(corpus, id2word=dictionary, num_topics=numTopics, passes=20)
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
      self.calculate_metrics(category_id, objs, labels, goldstandards)
    else:
      print "number of clusters equals or lower than 1, ignoring metric"

  def algorithm_lsi(self, category_id, objs, goldstandards):
    numTopics = self.calculate_k_using_firstnames(objs)
    print "Using k = "+str(numTopics)

    texts = []
    for obj in objs:
      texts.append(get_categorizedproduct_content(obj))

    dictionary = corpora.Dictionary(texts)
    corpus = [dictionary.doc2bow(text) for text in texts] # bag of words

    print "Create models"
    lsi_model = LsiModel(corpus, id2word=dictionary, num_topics=numTopics)
    corpus_lsi = lsi_model[corpus]
    print "Done creating models"

    results = []
    labels = []
    cont = 0
    for probabilities, obj in izip(corpus_lsi, objs):
      if probabilities:
        max_prop = max(probabilities, key=lambda item:item[1])[0]
      else:
        max_prop = "WARNING "+str(texts[cont])
      labels.append(max_prop)
      results.append(str(max_prop)+" # "+obj['name'].encode('utf8'))
      cont += 1
    results.sort()
    for r in results:
      print r

    topic_id = 0
    for topic in lsi_model.show_topics(num_words=5):
        print "TOPIC (LSI2) " + str(topic_id) + " : " + topic
        topic_id+=1

    if numTopics > 1:
      self.calculate_metrics(category_id, objs, labels, goldstandards)
    else:
      print "number of clusters equals or lower than 1, ignoring metric"

  def calculate_k_using_firstnames(self, objs):
    first_names = set([item['name'].split()[0] for item in objs])
    result = len(first_names)
    if result > 500:
      result = 500
    if result >= len(objs):
      result = len(objs)-1
    if result == 0:
      result = 1
    return result

  # NEW TESTS

  def run_sklearn_algorithm(self, category_id, objs, goldstandards, use_cluster_firstname=True):
    labels = self.clusterize_for_fourth_category_level(category_id, objs, goldstandards)   
    if use_cluster_firstname: 
      final_labels = self.clusterize_for_third_category_level_with_firstname(objs, labels)
      results = []
      for label, obj in izip(final_labels, objs):
        results.append(str(label)+" # "+obj['name'].encode('utf8'))
      results.sort()
      print "\nFinal clusterized results:"
      for r in results:
        print r
    else:
      final_labels = self.clusterize_for_third_category_level_with_algorithm(objs, labels)
      results = []
      for label, obj in izip(labels, objs):
        results.append(str(final_labels[label])+" # "+obj['name'].encode('utf8'))
      results.sort()
      print "\nFinal clusterized results:"
      for r in results:
        print r
    
    # if len(final_labels_per_obj) > 1:
    #   self.calculate_metrics(category_id, objs, final_labels_per_obj, goldstandards)
    # else:
    #   print "skipping"

  def clusterize_for_fourth_category_level(self, category_id, objs, goldstandards, use_binary_search=False):
    # produce the fourth level of clusterization
    max_k_value = self.calculate_max_k_value(objs)
    result_max = self.run_sklearn_algorithm_step(category_id, objs, goldstandards, max_k_value)
    if use_binary_search:
      (avg_silhouette, category_id, labels, results) = self.run_binary_search(category_id, objs, goldstandards, max_k_value, result_max)
    else:
      (avg_silhouette, category_id, labels, results) = result_max
    return labels

  def clusterize_for_third_category_level_with_algorithm(self, objs, labels):
    labels_groups = defaultdict(list)
    for label, obj in izip(labels, objs):
      labels_groups[label] += get_categorizedproduct_content(obj, extra_name_weight=True)

    values_group = []
    label_order = []
    for label, values in labels_groups.iteritems():
      label_order.append(label)
      values_group.append(' '.join(values))
    vectorizer = TfidfVectorizer(stop_words=WORDS_STOPLIST, tokenizer=None, strip_accents=None, preprocessor=None, lowercase=False)
    tfidf_model = vectorizer.fit_transform(values_group)
    k_value = len(label_order) // 3
    print "Using k = "+str(k_value)+" for third level"
    model = AgglomerativeClustering(n_clusters=k_value, linkage="complete")
    third_level_labels = model.fit_predict(tfidf_model.toarray())

    final_labels = {}
    for label, third_level_label in izip(label_order, third_level_labels):
      final_labels[label] = third_level_label

    return final_labels

  def clusterize_for_third_category_level_with_firstname(self, objs, labels, use_prepositions=True):
    # given objs and labels from the fourth level, produce the third level of clusterization

    # first group the data for each cluster and define a label for each cluster's item (a normalized firstname)
    labels_groups = defaultdict(list)
    for label, obj in izip(labels, objs):
      normalized_name = get_terms(obj['name'], use_preposition=True, use_stemming=True, remove_numbers=False, remove_hifen=False, use_name_blacklist=True, remove_parentheses=True, use_only_one_gram=True, remove_alone_numbers=True)
      normalized_firstname = normalized_name[0]
      if use_prepositions:
        if len(normalized_name) > 1 and (normalized_firstname in GENERIC_WORDS or normalized_name[1] in PREPOSITIONS_LIST_SHORT):
          if len(normalized_name) > 2 and normalized_name[1] in PREPOSITIONS_LIST_SHORT:
            normalized_firstname+= "+"+normalized_name[2]
          else:
            normalized_firstname+= "+"+normalized_name[1]
      labels_groups[label].append(normalized_firstname)

    # then for each cluster define its label as its major label.
    # if there is no major label, it will be processed after the others. 
    final_labels = {}
    to_posprocess = []
    for label, values in labels_groups.iteritems():
      counter=collections.Counter(values)
      if len(set(counter.values())) == 1 and len(counter.values()) > 1: # there is no major name for the label
        to_posprocess.append((label, values))
      else:
        most_common_label = max(set(values), key=values.count)
        final_labels[label] = most_common_label

    # for each one left to process, choose their label to group them in labels that already exists
    for label, values in to_posprocess:
      labels_that_already_exists = set(values).intersection(final_labels.values())
      if labels_that_already_exists: # posprocess
        final_labels[label] = labels_that_already_exists.pop()
      else:
        longer_label = max(set(values), key=len)
        final_labels[label] = longer_label

    final_labels_per_obj = []
    for label, obj in izip(labels, objs):
      final_labels_per_obj.append(final_labels[label])
    return final_labels_per_obj

  def calculate_max_k_value(self, objs):
    if len(objs) > 500:
      max_k_value = len(objs) // 4
    elif len(objs) > 10:
      max_k_value = len(objs) // 3
    else:
      max_k_value = len(objs) // 2
    if max_k_value >= len(objs):
      max_k_value = len(objs) - 1
    if max_k_value > 200:
      max_k_value = 200
    if max_k_value < 2:
      max_k_value = 2
    return max_k_value

  def run_binary_search(self, category_id, objs, goldstandards, max_k_value, result_max):
    min_k_value = int(numpy.rint(math.sqrt(len(objs))))
    if min_k_value < 2:
      min_k_value = 2
    result_min = self.run_sklearn_algorithm_step(category_id, objs, goldstandards, min_k_value)
    if max_k_value < min_k_value:
      max_k_value = min_k_value
    if min_k_value == max_k_value:
        (best_k_value, result_best) = (min_k_value, result_min)
    else:
      (best_k_value, result_best) = max([(min_k_value, result_min), (max_k_value, result_max)],key=lambda item:item[1][0])

    if max_k_value > 10:
      range_value = 2
    else:
      range_value = 1

    min_limit = min_k_value
    max_limit = max_k_value
    for i in range(range_value):
      if min_limit == max_limit:
        break
      mid_k_value = (min_limit+max_limit) // 2
      result_mid = self.run_sklearn_algorithm_step(category_id, objs, goldstandards, mid_k_value)
      (best_k_value, result_best) = max([(best_k_value, result_best), (mid_k_value, result_mid)],key=lambda item:item[1][0])
      min_limit = best_k_value
      max_limit = mid_k_value

    print "Best k value = "+str(best_k_value)
    self.cont_k_matches += 1
    if best_k_value == max_k_value:
      print "max wins"
      self.cont_k_matches_max_wins += 1
    return result_best

  def run_sklearn_algorithm_step(self, category_id, objs, goldstandards, numTopics):
    print "Using k = "+str(numTopics)

    # vectorizer = TfidfVectorizer(stop_words=WORDS_STOPLIST, analyzer=get_categorizedproduct_content, tokenizer=None, strip_accents=None, preprocessor=None, lowercase=False)
    # tfidf_model = vectorizer.fit_transform(objs)

    texts = []
    for obj in objs:
      texts.append(get_categorizedproduct_content(obj))
    dictionary = corpora.Dictionary(texts)
    corpus = [dictionary.doc2bow(text) for text in texts] # bag of words
    tfidf_model = TfidfModel(corpus)
    features = lil_matrix(((len(corpus), len(dictionary.keys()))))
    for corpus_id, item in enumerate(corpus):
      for (word_id, tfidf_value) in tfidf_model[item]:
        features[corpus_id, word_id] = tfidf_value

    if self.algorithm == 'kmeans':
      model = KMeans(n_clusters=numTopics, n_init=12, max_iter=400,  copy_x=False, n_jobs=2)
      # labels = model.fit_predict(tfidf_model)
      labels = model.fit_predict(features)
    elif self.algorithm == 'agglo':
      model = AgglomerativeClustering(n_clusters=numTopics, linkage="ward")
      # labels = model.fit_predict(tfidf_model.toarray())
      labels = model.fit_predict(features.toarray())
    else:
      raise SystemExit
    # model = AffinityPropagation(copy=False) # good
    # model = SpectralClustering(n_clusters=numTopics) # good
    # model = DBSCAN()  # good # allows cluster data as noisy, but can be used to predict k of clusters
    # model = FeatureAgglomeration(n_clusters=numTopics) # too slow
    # model = MiniBatchKMeans(n_clusters=numTopics) # basically the same as kmeans
    # model = MeanShift() # bad results, too slow
    # model = Ward(n_clusters=numTopics) # basically the same as AgglomerativeClustering
    # labels = model.fit_predict(tfidf_model.toarray())

    results = []
    for label, obj in izip(labels, objs):
      results.append(str(label)+" # "+obj['name'].encode('utf8'))
    
    (avg_silhouette, _) = self.calculate_avg_silhouette(objs, labels)
    result = (avg_silhouette, category_id, labels, results)
    return result

  #

  def calculate_avg_silhouette(self, objs, labels):
    texts = []
    for obj in objs:
      texts.append(get_categorizedproduct_content(obj))
    dictionary = corpora.Dictionary(texts)
    corpus = [dictionary.doc2bow(text) for text in texts] # bag of words
    tfidf = TfidfModel(corpus)
    features = lil_matrix(((len(corpus), len(dictionary.keys()))))
    for corpus_id, item in enumerate(corpus):
      for (word_id, tfidf_value) in tfidf[item]:
        features[corpus_id, word_id] = tfidf_value
    labels = numpy.array(labels)
    avgs = []
    for i in range(3):
      avgs.append(numpy.mean(metrics.silhouette_score(features, labels, metric='euclidean',sample_size=500)))
    avg_silhouette = numpy.around(numpy.mean(avgs), decimals=3)
    stddev_silhouette = numpy.around(numpy.std(avgs), decimals=3)
    return (avg_silhouette, stddev_silhouette)

  def calculate_metrics(self, category_id, objs, labels, goldstandards, temp=False):
    msg = ""
    if goldstandards:
      homogeneity_score = metrics.homogeneity_score(goldstandards, labels)
      completeness_score = metrics.completeness_score(goldstandards, labels)
      v_measure_score = metrics.v_measure_score(goldstandards, labels)
      adjusted_rand_score = metrics.adjusted_rand_score(goldstandards, labels)
      adjusted_mutual_info_score = metrics.adjusted_mutual_info_score(goldstandards, labels)
      self.homogeneity_scores.append(homogeneity_score)
      self.completeness_scores.append(completeness_score)
      self.v_measure_scores.append(v_measure_score)
      self.adjusted_rand_scores.append(adjusted_rand_score)
      self.adjusted_mutual_info_scores.append(adjusted_mutual_info_score)

    # try:
    (avg_silhouette, stddev_silhouette) = self.calculate_avg_silhouette(objs, labels)
    # except ValueError as e:
    #   if not temp:
    #     print "skipping"
    #     self.skips += 1
    #     avg_silhouette = -1.0
    #     stddev_silhouette = -1.0
    if not temp:
      if goldstandards:
        msg += "----\n"
        msg += ("Homogeneity: %0.3f\n" % homogeneity_score)
        msg += ("Completeness: %0.3f\n" %  completeness_score)
        msg += ("V-measure: %0.3f\n" % v_measure_score)
        msg += ("Adjusted Rand Index: %0.3f\n" % adjusted_rand_score)
        msg += ("Adjusted Mutual Information: %0.3f\n" % adjusted_mutual_info_score)
      msg += str(category_id)+": "+str(avg_silhouette)+" ( "+str(stddev_silhouette)+" ), k = "+str(len(set(labels)))
      print msg
      self.final_results.append(msg)
      self.macro_avgs.append(avg_silhouette)
      self.macro_stdevs.append(stddev_silhouette)
      self.micro_avgs.append(avg_silhouette*float(len(labels)))
      self.total_corpus += float(len(labels))
    return avg_silhouette

  def print_results(self):
    print "\nFINAL RESULTS:"
    if self.total_corpus > 0:
      print "micro avg: "+str(numpy.around(sum(self.micro_avgs)/self.total_corpus, decimals=3))
    if self.macro_avgs:
      print "macro avg: "+str(numpy.around(numpy.mean(self.macro_avgs), decimals=3))+" ( "+str(numpy.around(numpy.std(self.macro_stdevs), decimals=3))+" )"
    if self.homogeneity_scores:
      print "homogeneity_scores: "+str(numpy.around(numpy.mean(self.homogeneity_scores), decimals=3))
      print "completeness_scores: "+str(numpy.around(numpy.mean(self.completeness_scores), decimals=3))
      print "v_measure_scores: "+str(numpy.around(numpy.mean(self.v_measure_scores), decimals=3))
      print "adjusted_rand_scores: "+str(numpy.around(numpy.mean(self.adjusted_rand_scores), decimals=3))
      print "adjusted_mutual_info_scores: "+str(numpy.around(numpy.mean(self.adjusted_mutual_info_scores), decimals=3))
    print
    self.final_results.sort()
    for r in self.final_results:
      print r
    print "skips: "+str(self.skips)

def file_generator(filename):
  for line in open("data-clusterization/"+filename):
    yield line

if (__name__ == '__main__'):
  print "read inputs, normalize and tokenize"
  texts_per_cat = defaultdict(list)
  for (domain, input_type, obj) in filter_and_classify_input(file_generator(filename), convert_to_version=None):
    texts_per_cat[obj.get('chaordicCategoryBidId', 'None')].append(obj)
  a = AlgorithmsWrapper(texts_per_cat, algorithm='agglo')
  a.run()
