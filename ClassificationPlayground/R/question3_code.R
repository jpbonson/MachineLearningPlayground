library(caret) # for train/test partition
library(randomForest) # randon forest classifier
library(klaR) # naive Bayes classifier

# REMEMBER: The indices start at 1! (not zero!)

# (c) Using 10-fold cross-validation, [1] train and evaluate a random forest and [2] a naïve Bayes classifier. 
# [3] Compare the accuracy of the two methods in terms of mean (𝜇) and standard deviation (𝜎) 
#  of accuracy in 10 folds. 
# [4] Eventually use a statistical significance test (e.g. student’s t-test) and determine whether the two 
# methods are significantly different or not. Use 𝛼=0.05 as the significance threshold.

# [0] preparation

data <- read.csv(file="winequality_simple.csv", header=TRUE, sep=",", colClasses=c(rep('numeric', 11), 'factor'))
set.seed(6000)
trainIndex <- createDataPartition(data$quality, p = .7, list = FALSE, times = 1)
train_data <- data[trainIndex,]
test_data <- data[-trainIndex,]
train_truth <- train_data[,12]
train_data_no_labels <- train_data[,1:11]
test_truth <- test_data[,12]
test_data_no_labels <- test_data[,1:11]
attach(train_data)
train_control <- trainControl(method="repeatedcv",number=10, repeats=1)

# [1] train and evaluate a random forest using 10-fold cross-validation

model_rf <- train(train_data_no_labels, train_truth, trControl=train_control, method="rf")
model_rf
test_pred_rf <- predict(model_rf, test_data_no_labels, type="raw")
confusionMatrix(test_pred_rf, test_truth)

# [2] train and evaluate a naïve Bayes classifier using 10-fold cross-validation

model_nb <- train(train_data_no_labels, train_truth, trControl=train_control, method="nb")
model_nb
test_pred_nb <- predict(model_nb, test_data_no_labels, type="raw")
confusionMatrix(test_pred_nb, test_truth)
# report: tried using preprocessing to improve the results, but no significant result was obtained
# warning: In density.default(xx, ...) : non-matched further arguments are disregarded
# report: maybe the assumption of uncorrelated variables is wrong for this dataset

# [3] compare the accuracy and std. dev. of the two methods in the 10 folds
# [REPORT]

# [4] determine if the methods are significantly different (0.05)
rf_accs <- NULL
nb_accs <- NULL
for (i in 1:10) {
    set.seed(i)
    model_rf <- train(train_data_no_labels, train_truth, trControl=train_control, method="rf")
    test_pred_rf <- predict(model_rf, test_data_no_labels, type="raw")
    rf_acc <- confusionMatrix(test_pred_rf, test_truth)$overall['Accuracy']
    rf_accs <- append(rf_accs, rf_acc)
    model_nb <- train(train_data_no_labels, train_truth, trControl=train_control, method="nb", laplace = 1)
    test_pred_nb <- predict(model_nb, test_data_no_labels, type="raw")
    nb_acc <- confusionMatrix(test_pred_nb, test_truth)$overall['Accuracy']
    nb_accs <- append(nb_accs, nb_acc)
    # naive bayes is calculated based on the data, and dont use random variables, so the results dont change for different seeds
}
rf_accs
nb_accs
wilcox.test(rf_accs,nb_accs)
