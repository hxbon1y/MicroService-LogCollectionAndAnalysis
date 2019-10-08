# MicroService-LogCollectionAndAnalysis
Log collection and analysis is not easy to software based on structure of micro services. Because a single function is splited as multiple sub functions, and a sub function is knowned as a service,  a service has mutiple instances generally when it was deployed and run on target machines.Just as distribution, the function is stronger and has better performance, but it is difficult to analyze the log if encounter some troubles on function. If the function is single, the log is easy to analyse, so if we collect the logs of micro services, sort it as time and output it into  a single file, analyze it like a  single softwore, it will be easy again.

基于微服务架构的软件日志分析是一个难点。一个功能完整的软件按照微服务的架构被划分成多个子功能实现的若干服务，每个服务按照部署或运行分成多个实例，这样提高了程序的并发性能和容错性，是软件基于云进化的趋势，但同时带来了定位软件问题的难度，过去单一软件因为日志集中，可以很简单的定位到问题，现在出错的问题具体到哪个服务或者到哪个实例得一步步分析，没有像单一软件那样方便。如果我们能按照向单一软件去分析微服务的日志，定位问题可以大大的得到简化，本工程就是基于这样一种思想将微服务日志的软件合一，并按照业务时间排序，输出到一个单一文件中。这样分析微服务的日志将非常方便。
