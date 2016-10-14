import pandas
import matplotlib as plt
from s3Fetcher import s3Fetcher # lib do engine, em engine-tools

# para mostrar no python notebook
plt.style...
matplotlib inline

data = s3Fetcher().bucket('platform-dumps-virginia').query('latest-products/saraiva-v5.gz')
# na query da para pegar um range de datas

products = next(data)
products = DataFrame(next(data)) #[['id', 'name', 'price']] .setIndex('id')
products.head() # equivalente: products[:10]
#

products['apiKey'] # ou .apiKey
#

# dataframe e' composto por series (colunas)

products.T # transpose
#

products.loc(3) # mostra a linha 3
#

products.price.asType(float).hist()

pandas.TimeStamp('today') - pandas.TimeStamp('2016-01-01').days

buyOrders.groupby('sku').size()

buyOrders.groupby('sku')['price'].mean().plot()

buyOrders.groupby('sku').apply(myFunc or lambda)

# reseample e' groupby paa dados temporais