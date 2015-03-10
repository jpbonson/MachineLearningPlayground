#!/usr/bin/env python
# encoding: utf-8
# -*- coding: utf-8 -*-
# vim:ts=4:et:nowrap
# vim: set fileencoding=utf-8 :
'''
Created on 12.07.2014
@author: Boenisch

- load the product data from disk
– split the data into training and test set
– vectorization of the product titles and TF.IDF normalization
– train the SVM classifier
– predict the categories for the test set
– evaluation
'''
from itertools import cycle
from sklearn import metrics
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_selection.univariate_selection import SelectKBest, chi2
from sklearn.svm import LinearSVC
import json
import numpy
import random
from unicodedata import normalize 
import unicodedata

N_train = 8000
N_test = 2000
cats = ["MLB1039","MLB1051","MLB1276","MLB1384","MLB1648"]

validation_mode = "test"
#validation_mode = "train"

N = N_train + N_test

print "load data"

cont1 = 0
cont2 = 0
data_dic = {}
for cat in cats:
	data = []
	with open("data/"+cat+".json","r") as f:
		for line in f:
			try:
				dic = json.loads(line)
				data.append(dic["title"])
				cont1 += 1
			except:
				cont2 += 2
	 
	random.shuffle(data)
	data_dic[cat] = data

print str(cont1)
print str(cont2)

print "data preprocessing"

data_train = []
data_test = []

for cat in cats:
	data = data_dic[cat]
	data_train += data[0:N_train]
	data_test += data[N_train:N]

X_train = numpy.array(data_train)
X_test = numpy.array(data_test)

y_train = numpy.empty_like(X_train)
y_test = numpy.empty_like(X_test)

idx_start = 0
for cat in cats:
	idx_end = idx_start + N_train
	y_train[idx_start:idx_end] = cat
	idx_start += N_train

idx_start = 0
for cat in cats:
	idx_end = idx_start + N_test
	y_test[idx_start:idx_end] = cat
	idx_start += N_test
 
print X_train.shape, y_train.shape
print X_test.shape, y_test.shape

print "start classification"

# vectorization
vectorizer = TfidfVectorizer(strip_accents="unicode", ngram_range=(1,1))
X_train = vectorizer.fit_transform(X_train)
X_test = vectorizer.transform(X_test)

# feature reduction
ch2 = SelectKBest(chi2, k="all")
ch2.fit(X_train, y_train)
X_train = ch2.fit_transform(X_train, y_train)
X_test = ch2.transform(X_test)

# training
clf = LinearSVC()
clf.fit(X_train, y_train)

if validation_mode == "train":
	X_test = X_train
	y_test = y_train

# predict categories
predicted = clf.predict(X_test)

print numpy.mean(predicted == y_test)
print metrics.classification_report(y_test, predicted)

cm = metrics.confusion_matrix(y_test, predicted)
print cm