# also: http://www.rdatamining.com/examples/exploration
# https://github.com/pochuan/stats315a/blob/master/wineTree.R
# https://www.casact.org/education/spring/2012/handouts%5CSession_4966_handout_392_0.pdf

data(iris)
head(iris)

# Sepal.Length Sepal.Width Petal.Length Petal.Width Species
#   1          5.1         3.5          1.4         0.2  setosa
#   2          4.9         3.0          1.4         0.2  setosa
#   3          4.7         3.2          1.3         0.2  setosa
#   4          4.6         3.1          1.5         0.2  setosa
#   5          5.0         3.6          1.4         0.2  setosa
#   6          5.4         3.9          1.7         0.4  setosa

# model fitting
fitTree<-rpart(Species~Sepal.Length+Sepal.Width+Petal.Length+Petal.Width,iris)

# > fitTree
# n= 150 

# node), split, n, loss, yval, (yprob)
#       * denotes terminal node

# 1) root 150 100 setosa (0.33333333 0.33333333 0.33333333)  
#   2) Petal.Length< 2.45 50   0 setosa (1.00000000 0.00000000 0.00000000) *
#   3) Petal.Length>=2.45 100  50 versicolor (0.00000000 0.50000000 0.50000000)  
#     6) Petal.Width< 1.75 54   5 versicolor (0.00000000 0.90740741 0.09259259) *
#     7) Petal.Width>=1.75 46   1 virginica (0.00000000 0.02173913 0.97826087) *

# prediction-one row data
newdata<-data.frame(Sepal.Length=7,Sepal.Width=4,Petal.Length=6,Petal.Width=2)

# > newdata
# Sepal.Length Sepal.Width Petal.Length Petal.Width
#   1            7           4            6           2

# perform prediction
predict(fitTree, newdata,type="class")
  #    1 
  # virginica 
  # Levels: setosa versicolor virginica

#prediction-multiple-row data
newdata2<-data.frame(Sepal.Length=c(7,8,6,5),Sepal.Width=c(4,3,2,4),Petal.Length=c(6,3.4,5.6,6.3),Petal.Width=c(2,3,4,2.3))

# > newdata2
#   Sepal.Length Sepal.Width Petal.Length Petal.Width
#    1            7           4          6.0         2.0
#    2            8           3          3.4         3.0
#    3            6           2          5.6         4.0
#    4            5           4          6.3         2.3

# perform prediction
predict(fitTree,newdata2,type="class")
 #      1         2         3         4 
 # virginica virginica virginica virginica 
 # Levels: setosa versicolor virginica
