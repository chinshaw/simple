N <- 1e4
DF <- data.frame(num=rep("foo", N), txt=rep("", N), stringsAsFactors=FALSE)
         
while(TRUE) {
	DF[nrow(DF)+1, ] <- rep(c(1027,1028,1030,1032,1037),each=10000000)
}