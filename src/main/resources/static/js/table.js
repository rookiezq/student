layui.use('table', function () {
    var table = layui.table;
    //第一个实例
    table.render({
        elem: '#test'
        , height: 500
        , url: '/s' //数据接口
        , page: true //开启分页
        , method: 'post'
        , response: {
            statusName: 'code' //规定数据状态的字段名称，默认：code
            , statusCode: 0 //规定成功的状态码，默认：0
            , msgName: 'msg' //规定状态信息的字段名称，默认：msg
            , countName: 'count' //规定数据总数的字段名称，默认：count
            , dataName: 'data' //规定数据列表的字段名称，默认：data
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "code": res.status, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.total, //解析数据长度
                "data": res.data.stuList //解析数据列表
            };
        }
        , cols: [
            [ //表头
            ]
        ]
        , done: function (res, curr, count) {
            console.log(res);
            var courses = res.data.courses;
            var tableHeaders = [];
            for (var i = 2; i < courses.length; i++) {
                tableHeaders[i] = {
                    field: 'course' + (i - 1),
                    title: courses[i - 2],
                    align: 'center',
                    width: '120',
                    sort: true,
                    totalRow: true
                };
            }
            tableHeaders[0] = {type: 'checkbox', fixed: 'left'};
            tableHeaders[1] = {
                field: 'stuid', title: '学号',
                align: 'center', width: '120', sort: true
            };
            table.init('test', {
                cols: [tableHeaders]
                , data: res.data.stuList
                , limit: 10
            });
        }
    });
});
