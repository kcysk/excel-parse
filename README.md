# excel-parse

### 使用POI解析Excel结合平时业务，提供一个基础导入服务

#### 1、使用扩展说明

    核心类
    1、net.zdsoft.dataimport.biz.AbstractImportBiz，具体导入业务类继承该类实现方法即可
    2、net.zdsoft.dataimport.biz.AbstractImportAction Controller 继承该类，可设置是否走任务模式（任务模式未实现）还是实时模式
    3、