## Methods for classes

setMethod("show", 
	"MetricNumber",
	function(object){
		cat("MetricNumber=", object@value ," \n ")
	} 
)

setMethod("show", 
	"MetricNumber",
	function(object){
		cat("MetricNumber=", object@value ," \n ")
	} 
)

setGeneric("addViolation",
  def = function(object, violation,...) {
    standardGeneric("addViolation")
  }
)


setMethod("addViolation",
  signature = c(object="Metric", violation="Violation"),
  function(object, violation, ...) {
		c(object@violations, violation);
  }
)

setGeneric("addRange",
  def = function(min, max, criticality,...) {
    standardGeneric("addRange")
  }
)

setMethod("addRange",
	"MetricNumber",
	function(min="numeric", max="numeric", criticality="numeric", ...) {
		range <- MetricRange(min=min, max=max, criticality=criticality)
		c(object@ranges, range)
	}
)

setGeneric("setPlot",
  def = function(plot,...) {
    standardGeneric("setPlot")
  }
)

setMethod("setPlot",
  signature = c(plot="character"),
  function(plot, ...) {
		readBin(plot, what="raw", n=1e6)
	}
)
