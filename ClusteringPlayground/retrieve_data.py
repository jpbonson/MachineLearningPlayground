#!/usr/bin/env python
# encoding: utf-8
# -*- coding: utf-8 -*-
# vim:ts=4:et:nowrap
# vim: set fileencoding=utf-8 :
'''
Created on 12.07.2014
@author: Boenisch

– download the product metadata for the categories MLB1039, MLB1051, MLB1276, MLB1384 and
MLB1648 via the MercadoLivre API
– save the product ids and titles in the files MLB1039.json, MLB1051.json, MLB1276.json, MLB1384.json
and MLB1648.json, respectively

'''
import json
import urllib2

cats = ["MLB1039"] #,"MLB1051","MLB1276","MLB1384","MLB1648"]

for cat in cats:
	base_url = "https://api.mercadolibre.com/sites/MLB/search?category="+cat+"&limit=200"
	f = open("data/"+cat+".json","w")
	for offset in range(0,10000,200):
		print offset
		dic = json.load(urllib2.urlopen(base_url+"&offset="+str(offset)))
		for res in dic["results"]:
			f.write('{"id":"'+res["id"].encode('utf-8')+'", "title":"'+res["title"].encode('utf-8')+'", "cat1":"'+cat.encode('utf-8')+'", "cat2":"'+res["category_id"].encode('utf-8')+'"}')
			f.write('\n')