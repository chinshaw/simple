##
# Makefile for building VF R analytics packge.



all:
	R CMD build --binary VFAnalyticsPackage


install: all 
	R CMD install VFAnalyticsPackage


test:
	R --vanilla < MetricTest.R

clean:
	rm -fr VFAnalyticsPackage.tgz
