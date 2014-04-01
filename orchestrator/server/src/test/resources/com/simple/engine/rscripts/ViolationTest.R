library("VFAnalytics")

metric1 <- MetricNumber(value=101)
metric2 <- MetricNumber(value=102)

violation1 <- Violation(subgroup=10, severity=3, rule="COMING FROM SCRIPT THERE IS A PROBLEM", detail="You broke it so fix it")


metric1@violations <- addViolation(metric1, violation1)

print(metric1);
print(metric1@violations);
