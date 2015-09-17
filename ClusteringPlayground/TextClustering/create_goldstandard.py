#!/usr/bin/env python
# encoding: utf-8
# -*- coding: utf-8 -*-
# vim:ts=4:et:nowrap
# vim: set fileencoding=utf-8 :

import json
from test_project.normalize import normalize_string, normalize_diacritics

j_content = []
with open("data-clusterization/blah") as f:
    for line in f:
        j_content.append(json.loads(line))

results = []
for item in j_content:
    item['name'] = normalize_string(item['name'])
    item['goldstandard'] = item['name'].split()[0]
    results.append(item)

with open('data-clusterization/blah_goldstandard', 'w+') as outfile:
    for hostDict in results:
        json.dump(hostDict, outfile)
        outfile.write('\n')
