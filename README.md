# 配置中心
##dashboard

###一、App 配置项

####说明：
#####数据结构key-value
#####zookeeper节点结构：
>
#####.../AppConfigs/
#####.../AppConfigs/${项目名}
#####.../AppConfigs/${项目名}/${key}--${value}

###一、数据源配置

#####.../DataSource/
#####.../DataSource/${项目名}
#####.../DataSource/${项目名}/master/type--master或者slave
#####.../DataSource/${项目名}/master/confing  数据库连接池配置项
#####.../DataSource/${项目名}/slave${序号}/type--master或者slave
#####.../DataSource/${项目名}/slave${序号}/confg    数据库连接池配置项
