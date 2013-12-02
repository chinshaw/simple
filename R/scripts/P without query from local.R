#User input nsigma, numeric
nsigma<-'3'

#User input confidence_level, numeric

confidence_level<-''

#User input centerline, numeric
centerline<-''

#User input standard_deviation, numeric
standard_deviation<-''

#User select Rules options: 'Western Electric' or 'Westgard'
Rules<- 'Western Electric' 


#User select if the exclusions should be disused: 'Yes' or 'No'
disuse_exclusions<-'Yes'

#User select Phase_generation option: 'Yes' or 'No'
Phase_generation<-'No'

#User select Process_capability_Analysis option: 'Yes' or 'No'
Process_capability_Analysis<-'Yes'

#User input Process_capability_Analysis LCL, numeric
PCA_LCL<-'0'

#User input Process_capability_Analysis UCL, numeric
PCA_UCL<-'0.3'

#User select oc_curves option: 'Yes' or 'No'
oc_curves<-'Yes'











if(Process_capability_Analysis=='Yes'&(PCA_LCL==''|PCA_UCL==''))
  {stop("'PCA_LCL' and 'PCA_UCL' must be specified for Process capability Analysis") }


if(!(Process_capability_Analysis=='Yes'&(PCA_LCL==''|PCA_UCL==''))){




  #packages;
#library(RODBC);
library(qcc);
library(qualityTools)





if(nsigma==''){nsig<-''} 
if(nsigma!=''){nsig<-paste('nsigmas','=',nsigma,',')}
 

if(Phase_generation=='Yes'){if(confidence_level==''){confi<-''
                         nsigmaoutput<-paste('nsigma=Pchart[[i]] $ nsigmas',',')}}

if(Phase_generation!='Yes'){if(confidence_level==''){confi<-''
                         nsigmaoutput<-paste('nsigma=Pchart $ nsigmas',',')}}







if(confidence_level!=''){confi<-paste('confidence.level','=',confidence_level,',')
                         nsigmaoutput<-''}

if(centerline==''){cen<-''} 
if(centerline!=''){cen<-paste('center','=',centerline,',')}


if(standard_deviation==''){sd<-''} 
if(standard_deviation!=''){sd<-paste('std.dev','=',standard_deviation,',')}



#C:/Users/liuandyi/Desktop/Pchart_week_equal_sample_size.txt
#C:/Users/liuandyi/Desktop/Pchart_week_New.txt

Pchartdata<-read.table(file="/Users/chinshaw/devel/workspace/virtualFactory/R/Pchart_week_equal_sample_size.txt", header = TRUE)


if(disuse_exclusions=='Yes'){Pchartdata<- subset(Pchartdata, exclusion=='No')}


Totalphase<-max(Pchartdata$Phase)








if (Phase_generation=='Yes'){
  
  
  
  
  
  
  
  
Pchartdatanew<-list(Null)
for (i in 1:Totalphase ){
    Pchartdatanew[[i]] <- subset(Pchartdata,Phase==i)
    
}
  
for (i in 1:Totalphase ){if ((nrow(Pchartdatanew[[i]]))<20){stop("At least 20 groups of samples should be collected before calculating the statistics and control limit")}
}




Pchart<-list(Null)
for (i in 1:Totalphase ){

  
  
Pchart[[i]]<-eval(parse(text=paste("qcc", "(","data=Pchartdatanew[[i]]$Num_of_defects, type= 'p', sizes=Pchartdatanew[[i]]$Num_of_total",",",
                             nsig,confi,cen,sd,"labels=Pchartdatanew[[i]]$First_day_of_the_group,
                             rules = shewhart.rules,plot = FALSE, 
                             ylab='% Defective',xlab= '', axes.las=2", ")")))
}
 

plotucldata<-list(Null)
for (i in 1:Totalphase ) {plotucldata[[i]]<-data.frame(Pchart[[i]]$ statistics)[,1]}
plotucl <- max(unlist(plotucldata))+0.01

oldpar <- par(no.readonly = TRUE)


par(mfrow=c(1,Totalphase),"mar"=c(5.1, 4.1, 4.1, 0))


par("cex.axis"=0.8)
plot(Pchart[[1]], add.stats=FALSE, ylim=c(0,plotucl),ylab='% Defective',xlab= '', axes.las=2,title='Phase 1',restore.par=TRUE)
par(yaxt="n","mar"=c(5.1, 0, 4.1,0))
for (i in 2:(Totalphase-1) ) {plot(Pchart[[i]], add.stats=FALSE, ylim=c(0,plotucl),ylab='% Defective',xlab= '',axes.las=2,title=paste('Phase',i))}
par(yaxt="n","mar"=c(5.1, 0, 4.1,2.1))

plot(Pchart[[Totalphase]], add.stats=FALSE, ylim=c(0,plotucl),ylab='% Defective',xlab= '', axes.las=2,title=paste('Phase',Totalphase))



par(oldpar)

if (Process_capability_Analysis=='Yes'){

Process_capability<-list(Null)
  
for (i in 1:Totalphase )  {Process_capability[[i]]<-cp(x=plotucldata[[i]], "normal", lsl = as.numeric(PCA_LCL), usl = as.numeric(PCA_UCL), main= paste('Process capability Analysis','Phase',i)) }
  
} 

par(oldpar)

if(oc_curves=='Yes'){
  
oc<-list(Null)
  
for (i in 1:Totalphase )  { oc<-oc.curves(Pchart[[i]])}

}

par(oldpar)

Poutputdata<-list(Null)

for (i in 1:Totalphase ){

 Poutputdata[[i]] <-eval(parse(text=paste("data.frame", "(","p_data=Pchart[[i]]$ statistics,Defective=Pchart[[i]]$ data ,  Total_Quantity=
Pchart[[i]]$ sizes,P_Center=Pchart[[i]]$ center,Subgroup_Size=length(Pchart[[i]]$sizes), Phase=i,
p_stddev=Pchart[[i]]$ std.dev
  ",",",nsigmaoutput,"Pchart[[i]]$ limits", ")")))

Poutputdata[[i]]$Rules_Violated<-NULL
for(j in 1:length(Poutputdata[[i]]$p_data)){Poutputdata[[i]]$violations[j]<-ifelse (Poutputdata[[i]]$p_data[j] >= 
  Poutputdata[[i]]$LCL[j] && Poutputdata[[i]]$p_data[j] < Poutputdata[[i]]$UCL[j] , "No", "Yes")}

Poutputdata[[i]]$First_day_of_the_group <- Pchartdatanew[[i]]$First_day_of_the_group 
  
}

if(oc_curves=='No'&Process_capability_Analysis=='Yes'){Dataoutput<-c(Poutputdata=Poutputdata,Process_capability=Process_capability)}
if(oc_curves=='Yes'&Process_capability_Analysis=='No'){Dataoutput<-c(Poutputdata=Poutputdata,oc=oc)}
if(oc_curves=='No'&Process_capability_Analysis=='No'){Dataoutput<-Poutputdata}
if(oc_curves=='Yes'&Process_capability_Analysis=='Yes'){Dataoutput<-c(Poutputdata=Poutputdata,Process_capability=Process_capability,oc=oc)}
  
  
  
}


if (Phase_generation=='No'){
  
  
  


if ((nrow(Pchartdata ))<20){stop("At least 20 groups of samples should be collected before calculating the statistics and control limit")}


  
  
Pchart <-eval(parse(text=paste("qcc", "(","data=Pchartdata $Num_of_defects, type= 'p', sizes=Pchartdata $Num_of_total",",",
                             nsig,confi,cen,sd,"labels=Pchartdata $First_day_of_the_group,
                             rules = shewhart.rules,plot = FALSE, 
                             ylab='% Defective',xlab= '', axes.las=2", ")")))

 
plotucldata <-data.frame(Pchart $ statistics)[,1]
plotucl <- max(unlist(plotucldata))+0.01


oldpar <- par(no.readonly = TRUE)

par("cex.axis"=0.8)
plot(Pchart, ylim=c(0,plotucl),ylab='% Defective',xlab= '', axes.las=2)


par(oldpar)

if (Process_capability_Analysis=='Yes'){
Process_capability <-cp(x=plotucldata , "normal", lsl = as.numeric(PCA_LCL), usl = as.numeric(PCA_UCL)) }
  

par(oldpar)

if(oc_curves=='Yes'){
  
 oc<-oc.curves(Pchart )

}

par(oldpar)

 Poutputdata  <-eval(parse(text=paste("data.frame", "(","p_data=Pchart $ statistics,Defective=Pchart $ data ,  Total_Quantity=
Pchart $ sizes,P_Center=Pchart $ center,Subgroup_Size=length(Pchart $sizes), 
p_stddev=Pchart $ std.dev
  ",",",nsigmaoutput,"Pchart $ limits", ")")))

Poutputdata $Rules_Violated<-NULL
for(j in 1:length(Poutputdata $p_data)){Poutputdata $violations[j]<-ifelse (Poutputdata $p_data[j] >= 
  Poutputdata $LCL[j] && Poutputdata $p_data[j] < Poutputdata $UCL[j] , "No", "Yes")}

Poutputdata $First_day_of_the_group <- Pchartdata $First_day_of_the_group 
  


if(oc_curves=='No'&Process_capability_Analysis=='Yes'){Dataoutput<-c(Poutputdata=Poutputdata,Process_capability=Process_capability)}
if(oc_curves=='Yes'&Process_capability_Analysis=='No'){Dataoutput<-c(Poutputdata=Poutputdata,oc=oc)}
if(oc_curves=='No'&Process_capability_Analysis=='No'){Dataoutput<-Poutputdata}
if(oc_curves=='Yes'&Process_capability_Analysis=='Yes'){Dataoutput<-c(Poutputdata=Poutputdata,Process_capability=Process_capability,oc=oc)}
  
  
  
}

}
#Dataoutput is the output data file's name



