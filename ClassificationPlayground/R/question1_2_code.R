library(caret) # for train/test partition
library(rpart) # training a decision tree

# REMEMBER: The indices start at 1! (not zero!)

# (a) [1] Split the data randomly into a training set and a testing set (e.g. 65%‐35%).
# [2] Train a decision tree classifier using the train data.
# [3] Report the confusion matrix and accuracy for both train and test data.
# [4] Compare the train and test accuracy. Is there a big difference between train and test accuracy? Why?
# [5] Finally, visualize the tree.

# [1] read and partitionate data
data <- read.csv(file="winequality_simple.csv", header=TRUE, sep=",", colClasses=c(rep('numeric', 11), 'factor'))
set.seed(6000)
trainIndex <- createDataPartition(data$quality, p = .8, list = FALSE, times = 1)
train_data <- data[trainIndex,]
test_data <- data[-trainIndex,]
attach(train_data)

# [2] train tree 
control_obj <- rpart.control(minsplit = 30, minbucket = 7, cp = 0.0001, 
              maxcompete = 4, maxsurrogate = 5, usesurrogate = 2, xval = 10,
              surrogatestyle = 0, maxdepth = 30)
fit <- rpart(quality ~ fixed.acidity + volatile.acidity + citric.acid + residual.sugar + chlorides + free.sulfur.dioxide + total.sulfur.dioxide + density + pH + sulphates + alcohol, method="class", control=control_obj)
printcp(fit) # display cross-validation table
summary(fit) # detailed results of splits including surrogate splits

# [3] confusion matrix and accuracy (train and test)

# for testing:
# single_value<-data.frame(fixed.acidity=1,volatile.acidity=1,citric.acid=1,residual.sugar=1,chlorides=1,free.sulfur.dioxide=1,total.sulfur.dioxide=1,density=1,pH=1,sulphates=1,alcohol=1)
# single_value2<-test_data_no_labels[1,]

# train:
train_truth <- train_data[,12]
train_pred <- predict(fit, type="class")
confusionMatrix(train_pred, train_truth)

# test:
test_data_no_labels <- test_data[,1:11]
test_truth <- test_data[,12]
test_pred <- predict(fit, test_data_no_labels, type="class")
confusionMatrix(test_pred, test_truth)

# [4]
# [REPORT]

# [5] plot tree 
pdf("tree.pdf", width = 16, height = 10 )
plot(fit, uniform=TRUE, main="Classification Tree for Wine Quality") # plot decision tree
text(fit, use.n=TRUE, all=TRUE, cex=.8) # label the decision tree plot
# post(fit, file = "tree.ps", title = "Classification Tree for Wine Quality") # create postscript plot of decision tree

# (b) [1] Repeat (a) and after training the classifier, prune the decision tree.
# [2] Report the confusion matrix and accuracy for train and test data using the pruned tree and
#  compare it to those obtained from (a).
# [3] Visualize the tree and discuss the effect of pruning on generalization ability of the model.
# [4] How the train and test accuracy changes by pruning? Why?

# [1] prune the tree
# cp (complexity parameter): choose it so it minimizes the xerror
pfit <- prune(fit, cp = fit$cptable[which.min(fit$cptable[,"xerror"]),"CP"])
printcp(pfit) # display cross-validation table
summary(pfit) # detailed results of splits including surrogate splits

# [2] confusion matrix and accuracy (train and test)

# train:
train_truth <- train_data[,12]
train_pred <- predict(pfit, type="class")
confusionMatrix(train_pred, train_truth)

# test:
test_data_no_labels <- test_data[,1:11]
test_truth <- test_data[,12]
test_pred <- predict(pfit, test_data_no_labels, type="class")
confusionMatrix(test_pred, test_truth)

# [REPORT]

# [3] plot tree
pdf("ptree.pdf", width = 16, height = 10 )
plot(pfit, uniform=TRUE, main="Classification Pruned Tree for Wine Quality") # plot decision tree
text(pfit, use.n=TRUE, all=TRUE, cex=.8) # label the decision tree plot

# [REPORT]

# [4] 
# [REPORT]