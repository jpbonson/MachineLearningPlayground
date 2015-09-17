#!/usr/bin/env python
# encoding: utf-8
# -*- coding: utf-8 -*-
# vim:ts=4:et:nowrap
# vim: set fileencoding=utf-8 :

import json
from test_project.normalize import normalize_string, normalize_diacritics

# def download(s3_manager, data_name, s3file):
#     print "Getting S3 path"
#     s3_path = s3_manager.get_s3_path(data_name)
#     print "Downloading"
#     s3_manager.download_files(s3_path, data_name)
#     file_name = '/mnt/tmp/engine/'+data_name
#     s3data = {data_name: s3_path}
#     json.dump(s3data, s3file)
#     s3file.write('\n')
#     print "File created at: "+file_name

# if __name__ == "__main__":
#     s3_manager = S3Manager()
#     with open(s3_manager.get_file_path("s3_paths"),"w+") as f:
#         download(s3_manager, 'chaordic-products', f)
#         download(s3_manager, 'content_based_inputs', f)
#     # TODO: resetar server ou chamar clear cache

fantasia = ["0", "4", "5", "7", "8", "9", "46", "71"]
supl = ["62", "63", "43", "64", "75", "18"]

cont = 0

namemap = {
	'4': ["Moda e Acessórios", "Moda Infantil"],
	'6': ["Brinquedos","Fantasias e Acessórios"], 
	'16': ["Moda e Acessórios", "Tênis e Chuteiras"],
	'68': ["Alimentos e Bebidas","Suplementos Alimentares"],
	'71': ["Moda e Acessórios", "Moda íntima"]
}

j_content = []
with open("contentbased_only5") as f:
    for line in f:
        j_content.append(json.loads(line))

results = []
for item in j_content:
	item['name'] = normalize_string(item['name'])
	item['description'] = normalize_string(item['description'])
	item['clientCategory'] = [normalize_string(t) for t in item['clientCategory']]
	item['chaordicCategory'] = []
	results.append(item)

j_content = results
results = []
for item in j_content:
	current = item['chaordicCategoryBidId']
	apikey = item['apikey']
	item['clientCategory'] = filter(None, item['clientCategory'])
	if current == '5':
		all_words = [normalize_string(t) for t in item['clientCategory']]
		all_words = ' '.join(all_words)
		print str(all_words)
		if (('tenis' in all_words or 'sneaker' in all_words or 'chuteira' in all_words or 'running' in all_words or 'futsal' in all_words) and not 'sapatenis' in all_words):
			new_cat = "16"
			item['chaordicCategoryBidId'] = new_cat
			item['chaordicCategoryBidId_old'] = current
			cont += 1
	results.append(item)

j_content = results
results = []
for item in j_content:
	current = item['chaordicCategoryBidId']
	apikey = item['apikey']
	item['clientCategory'] = filter(None, item['clientCategory'])
	if current == '5':
		all_words = normalize_string(item['name'])
		all_words += normalize_string(item['description'])
		print str(all_words)
		if (('tenis' in all_words or 'sneaker' in all_words or 'chuteira' in all_words or 'running' in all_words or 'futsal' in all_words) and not 'sapatenis' in all_words):
			new_cat = "16"
			item['chaordicCategoryBidId'] = new_cat
			item['chaordicCategoryBidId_old'] = current
			cont += 1
	results.append(item)

print "cont "+str(cont)

with open('contentbased_only5_2', 'w+') as outfile:
    for hostDict in results:
        json.dump(hostDict, outfile)
        outfile.write('\n')

# # import json

# # catmap = {'6': '55', '16': '21', '22': '3', '32': '24', '41': '21', '53': '21', '68': '67'}
# # namemap = {
# # 	'3': ["Linha Industrial","Linha Industrial em Geral"], 
# # 	'12': ["Informática","Projetor Multimídia e Acessórios"],
# # 	'21': ["Serviços","Serviços em Geral"], 
# # 	'24': ["Eletrodomésticos","Lava-Roupas e Secadoras"], 
# # 	'26': ["Eletrodomésticos","Adegas"],
# # 	'55': ["Jóias e Relógios","Jóias e Relógios em Geral"], 
# # 	'67': ["Mídia","CDs, DVDs e Blu-Ray"],
# # 	'69': ["Livros e Cursos","Livros e Cursos em Geral"]
# # }

# # j_content = []
# # with open("contentbased_supl_fant") as f:
# #     for line in f:
# #         j_content.append(json.loads(line))

# # results = []
# # for item in j_content:
# # 	current = item['chaordicCategoryBidId']
# # 	if current == "":
# # 		item['chaordicCategoryBidId'] = '5'
# # 		item['chaordicCategoryBidId_old'] = ""
# # 		print "- (removed) id changed "+str(item['productId'])+" from "+str(current)+" to "+str(5)
# # 		continue
# # 	new_cat = catmap.get(current)
# # 	if current in catmap:
# # 		item['chaordicCategoryBidId'] = new_cat
# # 		item['chaordicCategoryBidId_old'] = current
# # 		print "- id changed "+str(item['productId'])+" from "+str(current)+" to "+str(new_cat)
# # 	catid = item['chaordicCategoryBidId']
# # 	if catid in namemap:
# # 		oldcats = item['chaordicCategory']
# # 		item['chaordicCategory'] = namemap[catid]
# # 		print "- names changed "+str(item['productId'])+" from "+str(oldcats)+" to "+str(item['chaordicCategory'])
# # 	results.append(item)

# # open('contentbased_supl_fant', 'w').close()
# # with open('contentbased_supl_fant', 'a') as outfile:
# #     for hostDict in results:
# #         json.dump(hostDict, outfile)
# #         outfile.write('\n')