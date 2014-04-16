library("VFAnalytics")



nan1 <- 0 / 0;

inf1 <- 12 / 0;

##### Metric Matrix

val1 <- c(77,78.1,78.2,78.8,79.7,79.9,81.1,81.2,81.8,82.8)

dataFrame <- data.frame(column1=val1)
metricMatrix <- MetricMatrix(dataFrame)

print(metricMatrix)

