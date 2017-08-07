<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>东莞学生调入调出</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${request.contextPath}/webjarsLocator/css/bootstrap.min.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="${request.contextPath}/webjarsLocator/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

    <![endif]-->
    <link rel="stylesheet" href="${request.contextPath}/webjarsLocator/css/pages.css">
    <!-- inline styles related to this page -->
</head>

<body>

<!-- /section:basics/sidebar -->
<div class="main-content">
    <div class="main-content-inner">
        <div class="page-content">

            <div class="row">
                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="import-wrap">
                        <div class="import-header">
                            <h4 class="import-title">任课信息批量导入步骤：</h4>
                        </div>
                        <div class="import-body">
                            <div class="import-step clearfix">
                                <span class="import-step-num">1、</span>
                                <div class="import-content">
                                    <p>下载模板：<a href="javascript:void(0);">任课信息导入模板</a></p>
                                </div>
                            </div>
                            <div class="import-step clearfix">
                                <span class="import-step-num">2、</span>
                                <div class="import-content">
                                    <p>根据模板填写信息并选择相应文件</p>
                                    <p>
                                        <button class="btn btn-sm btn-lightblue">
                                            上传相应文件
                                        </button>
                                        <strong>文件上传过程中，请勿关闭或离开页面</strong>
                                    </p>
                                    <ul class="import-filelist">
                                        <li><a href="">任课信息导入模板.xls</a></li>
                                        <li><a href="">教师信息.xls</a></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="import-step clearfix">
                                <span class="import-step-num">3、</span>
                                <div class="import-content">
                                    <p>选择任课信息相关属性</p>
                                    <div class="filter clearfix">
                                        <div class="filter-item">
                                            <label for="" class="filter-name">学年：</label>
                                            <div class="filter-content">
                                                <select class="form-control">
                                                    <option value=""></option>
                                                    <option value="2016">2016</option>
                                                    <option value="2015">2015</option>
                                                    <option value="2014">2014</option>
                                                    <option value="2013">2013</option>
                                                    <option value="2012">2012</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="filter-item">
                                            <label for="" class="filter-name">学期：</label>
                                            <div class="filter-content">
                                                <select class="form-control">
                                                    <option value=""></option>
                                                    <option value="2016">2016</option>
                                                    <option value="2015">2015</option>
                                                    <option value="2014">2014</option>
                                                    <option value="2013">2013</option>
                                                    <option value="2012">2012</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="filter-item">
                                            <label for="" class="filter-name">考试方式：</label>
                                            <div class="filter-content">
                                                <select class="form-control">
                                                    <option value=""></option>
                                                    <option value="2016">2016</option>
                                                    <option value="2015">2015</option>
                                                    <option value="2014">2014</option>
                                                    <option value="2013">2013</option>
                                                    <option value="2012">2012</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="filter-item">
                                            <label for="" class="filter-name">成绩录入方式：</label>
                                            <div class="filter-content">
                                                <select class="form-control">
                                                    <option value=""></option>
                                                    <option value="2016">2016</option>
                                                    <option value="2015">2015</option>
                                                    <option value="2014">2014</option>
                                                    <option value="2013">2013</option>
                                                    <option value="2012">2012</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="import-footer">
                            <button class="btn btn-darkblue">开始导入</button>
                            <strong>您需要先上传相应文件并选择属性，才能开始导入</strong>
                        </div>
                    </div>
                    <!-- 导入说明开始 -->
                    <div class="widget-box widget-box-lightblue2 collapsed">
                        <div class="widget-header">
                            <h4 class="widget-title lighter">说明</h4>
                            <div class="widget-toolbar no-border">
                                <a href="#" data-action="collapse">
                                    <i class="ace-icon fa fa-chevron-down"></i>
                                </a>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main padding-12 no-padding-top no-padding-bottom">
                                <p>1、导入时，会根据班级记学科确认数据是否存在，如果系统中已经存在，则会覆盖导入列的信息，如果不存在，则会新增记录</p>
                                <p>2、导入文件中请确认数值；类型的小数位数及对应的年级班级，否则可能会导致数据不对</p>
                                <p>3、导入班级名称为年级名称+班级名称</p>
                            </div>
                        </div>
                    </div><!-- 导入说明结束 -->
                    <!-- 导入数据表格开始 -->
                    <table class="table table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>导入文件名</th>
                            <th>导入状态</th>
                            <th>导入时间</th>
                            <th>失败数据条数</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>1</td>
                            <td>教师信息.xls</td>
                            <td>
                                <p class="import-state import-state-current">导入中...</p>
                            </td>
                            <td>2016-08-11 8:00</td>
                            <td>0/12</td>
                            <td>
                                <a class="red" href="#">
                                    <i class="ace-icon fa fa-close bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>学生成绩信息.xls</td>
                            <td>
                                <p class="import-state import-state-waiting">排队中，前面还有<span>5</span>个文件</p>
                            </td>
                            <td>2016-08-11 8:00</td>
                            <td>0/12</td>
                            <td>
                                <a class="red" href="#">
                                    <i class="ace-icon fa fa-close bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td>学生成绩信息.xls</td>
                            <td>
                                <p class="import-state import-state-success">导入成功</p>
                            </td>
                            <td>2016-08-11 8:00</td>
                            <td>0/12</td>
                            <td>

                            </td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td>学生成绩信息学生成绩信息.xls</td>
                            <td>
                                <p class="import-state import-state-failed">导入失败</p>
                            </td>
                            <td>2016-08-11 8:00</td>
                            <td>0/12</td>
                            <td>
                                <a class="red" href="#">
                                    <i class="ace-icon fa fa-download bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                        </tbody>
                    </table><!-- 导入数据表格结束 -->
                    <!-- PAGE CONTENT ENDS -->
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.page-content -->
    </div>
</div><!-- /.main-content -->

<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
    <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a>
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script src="${request.contextPath}/webjarsLocator/jquery/jquery.js"></script>

<!-- <![endif]-->

<script>

</script>


</body>
</html>
