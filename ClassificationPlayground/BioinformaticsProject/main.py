import random
import math
import time
import copy
import re
import numpy as np
from sklearn import svm
from sklearn import cross_validation
from sklearn import feature_selection
from sklearn import preprocessing
from sklearn import metrics
from sklearn import ensemble
from sklearn import tree
from sklearn import utils

CURRENT_DIR = "C:/Users/jpbonson/DropboX/Dalhousie Winter 2015/Bioinformatics/BioInfProject/"
# CURRENT_DIR = ""

CONFIG_PREPROCESSING = {
    'chi2_threshold': 0.004,
    'variance_threshold': 0.0006, 
}

CONFIG_MODELS = {
    'runs_total': 100,
    'k_folds': 3,
    'SVM': {
        'class_weight': {1: 1000},
        'C': 1.0,
    },
    'step': 70,
}

CONFIG_DEBUG = {
    'crossvalidation_step': False,
}

class Algorithm:
    def __init__(self, data_name):
        self.data_name = data_name

    def preprocess(self):
        start = time.time()
        content = []
        with open(CURRENT_DIR+self.data_name, "r") as f:
            content = f.readlines()
            content = [X.strip('\n').strip() for X in content]
            content = [X.split(',') for X in content]
            content = [[y for y in X if y != ''] for X in content if '?' not in X]
        content = np.array(content)
        X = content[:,:-1]
        X = [[float(y) for y in X] for X in X]
        y = content[:,-1]
        new_y = []
        cont1 = 0
        cont0 = 1
        for value in y:
            if value == 'active':
                new_y.append(1)
                cont1+=1
            else:
                new_y.append(0)
                cont0+=1
        y = np.array(new_y)
        print "cont0: "+str(cont0)+", cont1: "+str(cont1)+", total: "+str(cont0+cont1)+", min accuracy: "+str(float(cont0)/float(cont0+cont1))

        min_maX_scaler = preprocessing.MinMaxScaler(copy=False) # normalize data between 0 and 1
        X = min_maX_scaler.fit_transform(X)

        print "total features: "+str(len(X[0]))

        # Recall that the chi-square test measures dependence between stochastic variables, so using this function "weeds out" the features that 
        # are the most likely to be independent of class and therefore irrelevant for classification.
        chi2, _ = feature_selection.chi2(X, y)
        cont = 0
        indeces = []
        for i, c in enumerate(chi2):
            if c > CONFIG_PREPROCESSING['chi2_threshold']:
                cont += 1
                indeces.append(i)
        cont_features2d = 0
        cont_features3d = 0
        for i in indeces:
            if i < 4826:
                cont_features2d += 1
            else:
                cont_features3d += 1
        print "total features (pos-selection): "+str(len(indeces))+", 2dfeatures: "+str(cont_features2d)+"/4825, 3dfeatures: "+str(cont_features3d)+"/582"
        X = X[:, indeces]

        # This feature selection algorithm looks only at the features (X), not the desired outputs (y), and can thus be used for unsupervised learning.
        # The default is to keep all features with non-zero variance, i.e. remove the features that have the same value in all samples.
        selector = feature_selection.VarianceThreshold(threshold=CONFIG_PREPROCESSING['variance_threshold'])
        X = selector.fit_transform(X)
        indeces = selector.get_support(indices=True) # selected features
        previous_cont_features2d = cont_features2d
        cont_features2d = 0
        cont_features3d = 0
        for i in indeces:
            if i < previous_cont_features2d:
                cont_features2d += 1
            else:
                cont_features3d += 1
        print "total features (pos-selection): "+str(len(X[0]))+", 2dfeatures: "+str(cont_features2d)+"/4825, 3dfeatures: "+str(cont_features3d)+"/582"

        print "writing files"
        # with open(CURRENT_DIR+self.data_name+"_preprocessed", "w") as f:
        # np.savetxt(CURRENT_DIR+self.data_name+"_preprocessed_X", X)
        # np.savetxt(CURRENT_DIR+self.data_name+"_preprocessed_y", y)
        X.tofile(CURRENT_DIR+self.data_name+"_preprocessed_X")
        # with open(CURRENT_DIR+self.data_name+"_preprocessed_X", "w") as output_file:
        #     output_file.write(u'#'+'\t'.join(str(e) for e in X.shape)+'\n')
        #     X.tofile(output_file)
        y.tofile(CURRENT_DIR+self.data_name+"_preprocessed_y")
        
        end = time.time()
        elapsed = end - start
        print("Elapsed time: "+str(elapsed)+" secs")
        print "done"

    # http://scikit-learn.org/stable/modules/generated/sklearn.feature_selection.RFECV.html#sklearn.feature_selection.RFECV
    def estimator(self):
        start = time.time()
        print "#### Running estimator"
        X = np.fromfile(CURRENT_DIR+self.data_name+"_preprocessed_X", dtype=float)
        y = np.fromfile(CURRENT_DIR+self.data_name+"_preprocessed_y", dtype=int)
        X = X.reshape((16592, 2167))

        X, y = utils.shuffle(X, y)

        rfecv = feature_selection.RFECV(estimator=svm.LinearSVC(class_weight=CONFIG_MODELS['SVM']['class_weight'], C=CONFIG_MODELS['SVM']['C']), 
            step=CONFIG_MODELS['step'], cv=cross_validation.StratifiedKFold(y, 3), scoring=metrics.make_scorer(metrics.matthews_corrcoef, greater_is_better=True))
        rfecv = rfecv.fit(X, y)
        print "n_features_ : " +str(rfecv.n_features_)
        print "support_ : " +str(rfecv.support_)
        print "ranking_ : " +str(rfecv.ranking_)
        print "grid_scores_ : " +str(rfecv.grid_scores_)
        print "estimator_ : " +str(rfecv.estimator_)

        msg = ""
        for index in range(10):
            good_ranked_features = []
            for i, r in enumerate(rfecv.ranking_):
                if r <= index+1:
                    good_ranked_features.append(i)
            print "good_ranked_features ("+str(index+1)+"), len: "+str(len(good_ranked_features))+", features: " +str(good_ranked_features)
            msg += "good_ranked_features ("+str(index+1)+"), len: "+str(len(good_ranked_features))+", features: " +str(good_ranked_features)

        end = time.time()
        elapsed = end - start
        print "Elapsed time (secs): "+str(elapsed)
        msg += "Elapsed time (secs): "+str(elapsed)

        localtime = time.localtime()
        pretty_localtime = str(localtime.tm_year)+"-"+str(localtime.tm_mon)+"-"+str(localtime.tm_mday)+"-"+str(localtime.tm_hour)+str(localtime.tm_min)+str(localtime.tm_sec)
        text_file = open(CURRENT_DIR+"best_feature/best_features-"+str(CONFIG_MODELS['step'])+"_"+pretty_localtime+".txt",'w')
        text_file.write(msg)
        text_file.close()

    def run(self):
        print "Configuration:\n"+str(CONFIG_MODELS)
        print "total samples: "+str(16592)
        print "total features: "+str(2167)
        print "\n"
        elapsed_times = []
        total_accuracies = []
        total_sensivities = []
        total_specifities = []
        total_matthews_corrcoefs = []
        features_len = []
        for run_id in range(CONFIG_MODELS['runs_total']):
            start = time.time()
            print "############ Run "+str(run_id+1)
            X = np.fromfile(CURRENT_DIR+self.data_name+"_preprocessed_X", dtype=float)
            y = np.fromfile(CURRENT_DIR+self.data_name+"_preprocessed_y", dtype=int)
            X = X.reshape((16592, 2167))

            X, y = utils.shuffle(X, y)

            # clf = ensemble.ExtraTreesClassifier()
            # clf = ensemble.RandomForestClassifier()
            # clf.fit(X, y)
            # features_importance = clf.feature_importances_
            # print "len(features_importance): "+str(len(clf.feature_importances_))
            # print "len(features): "+str(len(X[0]))
            # cont = 0.0
            # indeces = []
            # for i, f in enumerate(features_importance):
            #     if f > 0.0001:
            #         cont += 1.0
            #         indeces.append(i)
            # X = X[:, indeces]
            # print "cont: "+str(cont)
            # print "features importance: "+str(features_importance)
            
            # X = svm.LinearSVC(C=0.1, penalty="l1", dual=False).fit_transform(X, y)

            custom_indeces = [13, 22, 23, 29, 30, 31, 46, 49, 53, 55, 58, 59, 73, 91, 96, 98, 100, 105, 106, 122, 123, 133, 136, 139, 147, 167, 181, 195, 197, 198, 199, 203, 219, 225, 240, 244, 246, 255, 260, 272, 282, 285, 289, 319, 320, 325, 329, 332, 334, 335, 340, 344, 345, 348, 379, 380, 439, 443, 453, 454, 456, 459, 491, 505, 513, 514, 515, 516, 535, 537, 542, 551, 560, 567, 580, 586, 587, 609, 610, 625, 631, 632, 634, 635, 642, 643, 679, 685, 715, 718, 736, 786, 787, 808, 810, 824, 896, 917, 928, 929, 945, 947, 952, 955, 956, 974, 978, 985, 987, 1018, 1020, 1024, 1036, 1040, 1041, 1046, 1047, 1051, 1070, 1140, 1158, 1161, 1168, 1170, 1188, 1199, 1202, 1218, 1219, 1221, 1244, 1254, 1255, 1256, 1274, 1278, 1286, 1292, 1295, 1311, 1340, 1341, 1344, 1346, 1354, 1373, 1403, 1412, 1413, 1416, 1418, 1421, 1422, 1434, 1436, 1447, 1449, 1468, 1470, 1499, 1517, 1520, 1539, 1553, 1564, 1565, 1581, 1583, 1584, 1603, 1609, 1612, 1613, 1623, 1624, 1631, 1635, 1637, 1638, 1639, 1640, 1644, 1645, 1682, 1700, 1716, 1717, 1731, 1744, 1746, 1750, 1752, 1769, 1804, 1805, 1809, 1810, 1811, 1821, 1830, 1833, 1838, 1864, 1872, 1885, 1890, 1891, 1911, 1916, 1917, 1920, 1923, 1929, 1936, 1939, 1940, 1953, 1956, 1970, 1974, 1979, 1984, 1987, 1989, 1992, 1993, 1998, 2000, 2004, 2011, 2017, 2018, 2019, 2020, 2021, 2022, 2024, 2025, 2034, 2046, 2059, 2062, 2063, 2067, 2070, 2078, 2080, 2086, 2087, 2092, 2093, 2095, 2102, 2103, 2104, 2105, 2108, 2109, 2111, 2112, 2113, 2123, 2146, 2149, 2150, 2152, 2154]
            X = X[:, custom_indeces]

            print "total features (pos-selection): "+str(len(X[0]))
            features_len.append(len(X[0]))

            # This cross-validation object is a variation of KFold that returns stratified folds. The folds are made by preserving the percentage of samples for each class.
            skf = cross_validation.StratifiedKFold(y, n_folds=CONFIG_MODELS['k_folds'])

            fold = 1
            accuracies = []
            sensivities = []
            specifities = []
            matthews_corrcoefs = []
            for train_indeX, test_indeX in skf:
                print "-------- Run fold - "+str(fold)
                fold += 1

                if CONFIG_DEBUG['crossvalidation_step']: print "splitting"
                X_train, X_test = X[train_indeX], X[test_indeX]
                y_train, y_test = y[train_indeX], y[test_indeX]

                if CONFIG_DEBUG['crossvalidation_step']: print "training"
                # http://scikit-learn.org/stable/modules/generated/sklearn.svm.LinearSVC.html
                clf = svm.LinearSVC(class_weight=CONFIG_MODELS['SVM']['class_weight'], C=CONFIG_MODELS['SVM']['C'])
                # clf = svm.SVC(class_weight=CONFIG_MODELS['SVM']['class_weight'], C=CONFIG_MODELS['SVM']['C'], kernel=CONFIG_MODELS['SVM']['kernel'])
                clf.fit(X_train, y_train)

                if CONFIG_DEBUG['crossvalidation_step']: print "predicting"
                y_predicted = clf.predict(X_test)

                if CONFIG_DEBUG['crossvalidation_step']: print "### Results"
                if CONFIG_DEBUG['crossvalidation_step']: print "confusion matriX:"
                if CONFIG_DEBUG['crossvalidation_step']: print metrics.classification_report(y_test, y_predicted)
                if CONFIG_DEBUG['crossvalidation_step']: print "accuracy: "+str(metrics.accuracy_score(y_test, y_predicted))
                if CONFIG_DEBUG['crossvalidation_step']: print "sensivity: "+str(metrics.recall_score(y_test, y_predicted))
                confusion_matriX = metrics.confusion_matrix(y_test, y_predicted)
                specifity = float(confusion_matriX[1,1])/float(confusion_matriX[0,1]+confusion_matriX[1,1])
                if CONFIG_DEBUG['crossvalidation_step']: print "specifity: "+str(specifity)
                if CONFIG_DEBUG['crossvalidation_step']: print "matthews_corrcoef: "+str(metrics.matthews_corrcoef(y_test, y_predicted))
                accuracies.append(metrics.accuracy_score(y_test, y_predicted))
                sensivities.append(metrics.recall_score(y_test, y_predicted))
                specifities.append(specifity)
                matthews_corrcoefs.append(metrics.matthews_corrcoef(y_test, y_predicted))
            print "----------------"            
            print "accuracy, mean: "+str(np.mean(accuracies))+", std: "+str(np.std(accuracies))
            print "sensivity, mean: "+str(np.mean(sensivities))+", std: "+str(np.std(sensivities))
            print "specifity, mean: "+str(np.mean(specifities))+", std: "+str(np.std(specifities))
            print "matthews_corrcoef, mean: "+str(np.mean(matthews_corrcoefs))+", std: "+str(np.std(matthews_corrcoefs))
            total_accuracies += accuracies
            total_sensivities += sensivities
            total_specifities += specifities
            total_matthews_corrcoefs += matthews_corrcoefs
            end = time.time()
            elapsed = end - start
            elapsed_times.append(elapsed)
        print "###########################"
        print "Configuration:\n"+str(CONFIG_MODELS)
        print "FINAL RESULTS:\n"
        print "from "+str(2167)+" features, to selected features, mean: "+str(np.mean(features_len))+", std: "+str(np.std(features_len))
        print "accuracy, mean: "+str(np.mean(total_accuracies))+", std: "+str(np.std(total_accuracies))
        print "sensivity, mean: "+str(np.mean(total_sensivities))+", std: "+str(np.std(total_sensivities))
        print "specifity, mean: "+str(np.mean(total_specifities))+", std: "+str(np.std(total_specifities))
        print "matthews_corrcoef, mean: "+str(np.mean(total_matthews_corrcoefs))+", std: "+str(np.std(total_matthews_corrcoefs))
        print "Elapsed time (secs), mean: "+str(np.mean(elapsed_times))+", std: "+str(np.std(elapsed_times))+", total: "+str(sum(elapsed_times))

if __name__ == "__main__":
    data = "data/K8.data"
    a = Algorithm(data)
    # a.preprocess()
    a.run()
    # a.estimator()

# 16772 com missing values
# 16592 sem missing values

# 'variance_threshold': 0.001,
# 16592
# 5408
# 16592
# 2508

# 'variance_threshold': 0.0005,
# 16592
# 5408
# 16592
# 3881

# sensivity == recall for class 1, specificity == precision for class 1

# The Matthews correlation coefficient is used in machine learning as a measure of the quality of binary (two-class) classifications. 
# It takes into account true and false positives and negatives and is generally regarded as a balanced measure which can be used even 
# if the classes are of very different sizes. The MCC is in essence a correlation coefficient value between -1 and +1. A coefficient 
# of +1 represents a perfect prediction, 0 an average random prediction and -1 an inverse prediction. 

# A perfect predictor would be described as 100% sensitive (e.g., all sick are identified as sick) and 100% specific (e.g., all healthy are not identified as sick)

# - Set of approaches: Clean or normalize the data to remove data points with missing values; Stratified K-fold; SVM (more specifically, 
# the Linear Support Vector Classification model(LinearSVC)). And, if necessary, I will use the SMOTE algorithm. For feature filtering and 
# determining feature importance, I will pre-process the data using statistical analysis (e.g.: VarianceThreshold and Univariate feature 
# selection) and them further select features using one or more of these three methods: LinearSVC, Tree-based estimators, or Genetic Algorithm (GA).

# You have tried the default parameters, but it turns out that two parameters can exert a large influence on the final accuracy of the SVM: these are C 
# and y (gamma). C is the error penalty; higher values of C will induce heavier penalties on misclassified points, but can lead to overfitting (and 
# consequently bad generalization). Gamma modifies the shape of the kernel function while retaining the same degree.

# The C parameter tells the SVM optimization how much you want to avoid misclassifying each training example. For large values of C, the optimization 
# will choose a smaller-margin hyperplane if that hyperplane does a better job of getting all the training points classified correctly. Conversely, a 
# very small value of C will cause the optimizer to look for a larger-margin separating hyperplane, even if that hyperplane misclassifies more points.