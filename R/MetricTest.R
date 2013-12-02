library(VFAnalytics)

x <- new("MetricNumber", 
         minrange = new("MetricRange", min=0, max=100),
         midrange = new("MetricRange", min=100, max=400),
         maxrange = new("MetricRange", min=400, max=600),
         value = 135)

print(x)
