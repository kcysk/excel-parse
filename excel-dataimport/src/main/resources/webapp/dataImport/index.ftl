<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>${title?default("数据导入")}</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" href="${request.contextPath}/webjarsLocator/bootstrap/bootstrap.css" />
    <link rel="stylesheet" href="${request.contextPath}/webjarsLocator/font-awesome/font-awesome.min.css" />
    <!-- ace styles -->
    <link rel="stylesheet" href="${request.contextPath}/webjarsLocator/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />
    <link rel="stylesheet" href="${request.contextPath}/webjarsLocator/css/pages.css">
    <link rel="stylesheet" href="${request.contextPath}/webjarsLocator/layer/layer.css">
</head>

<body>

<!-- /section:basics/sidebar -->
<div class="main-content">
    <div class="main-content-inner">
        <div class="page-content">

            <div class="row">
                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="widget-box widget-box-lightblue2 <#--collapsed-->">
                        <div class="widget-header">
                            <h4 class="widget-title lighter">导入步骤</h4>
                            <div class="widget-toolbar no-border">
                                <a href="#" data-action="collapse">
                                    <i class="ace-icon fa fa-chevron-down"></i>
                                </a>
                            </div>
                        </div>
                        <div class="widget-body">
                            <div class="widget-main padding-12 no-padding-top no-padding-bottom">
                                <div class="import-step clearfix">
                                    <span class="import-step-num">1、</span>
                                    <div class="import-content">
                                        <p>下载Excel模板：
                                            <button class="btn btn-sm btn-lightblue" id="btn-download">
                                            下载模板
                                            </button>
                                        </p>
                                    </div>
                                </div>
                                <div class="import-step clearfix">
                                    <span class="import-step-num">2、</span>
                                    <div class="import-content">
                                        <p>
                                            <input type="file" id="file-input" name="excel">
                                            上传Excel文件：
                                            <span class="btn btn-sm btn-lightblue" id="upload-btn">
                                                上传文件
                                            </span>
                                        </p>

                                    </div>
                                </div>
                                <div class="import-step clearfix">
                                    <span class="import-step-num">3、</span>
                                    <div class="import-content">
                                    <p>导入Excel文件：
                                        <button class="btn btn-sm btn-lightblue" id="start-btn">开始导入</button>
                                    </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

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
<div id="layer-common">

</div>
<!-- basic scripts -->

<!--[if !IE]> -->
<script src="${request.contextPath}/webjarsLocator/jquery/jquery.js"></script>
<script src="${request.contextPath}/webjarsLocator/layer/layer.js"></script>

<!-- <![endif]-->

<script>
    $(function(){
        resetFilePosition(); //

        $("#upload-btn").mouseover(function () {
           $("#file-input").offset({"top":$("#upload-btn").offset().top});
           $("#file-input").css("z-index",99999);
        });

        $("#btn-download").unbind().bind("click",function () {
            $("#layer-common").load("${request.contextPath}/${action}/import/template/page", function () {
                var index = layer.open({
                    type: 1,
                    title:"模板下载",
                    content:$("#layer-common"),
                    maxmin:true,
                    btn:['下载','取消'],
                    btn1:function (index) {
                        var headers = new Array();
                        $("#layer-common").find("input[name='header']:checked").each(function () {
                            headers.push($(this).val());
                        })
                        location.href = "${request.contextPath}/${action}/import/download?header=" + headers;
                    },
                    btn2:function (index) {
                        layer.close(index);
                    }
                });
                layer.full(index);
                $(".layui-layer-btn").css("text-align","center");
            });
        });

        $("#start-btn").unbind().bind("click",function(){
            var formData = new FormData();
            formData.append("name", "excel");
            formData.append("file", $("#file-input")[0].files[0]);
            $.ajax({
                url : "${request.contextPath}/${action}/import/upload",
                data:formData,
                type: 'post',
                processData: false,
                contentType: false,
                dataType:'json',
                success:function (data) {
                    if ( data.success ) {
                        layer.msg("文件上传成功，稍后请在列表中查看导入数据")
                    }
                }
            })
        });
    });

    function resetFilePosition() {
        $("#file-input").css({"position":"absolute","-moz-opacity":"0","opacity":"0","filter":"alpha(opacity=0)","cursor":"pointer","width":$("#upload-btn").width() +20,"height":$("#upload-btn").height()+8});
        $("#file-input").offset({"left":$("#upload-btn").offset().left});
        $("#file-input").css({"display":""});
    }
</script>


</body>
</html>
