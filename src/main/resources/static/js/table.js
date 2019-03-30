table.render({
    elem: '#test'
    ,url: '/course'
    ,cols: [[]]
    //回调函数 res返回的data curr当前页码 count数据总量
    ,done: function(res, curr, count){
        var mycars=new Array();
        var tit=res.nem;
        var strs= new Array();
        strs=tit.split(",");
        for (var i=0;i<strs.length;i++)
        {
            mycars[i]={field:'ex'+i, title:strs[i], align:'center',width:'120'};
        }
        table.init('test',{
            cols:[mycars]
            ,data:res.data
            ,limit:1000
        });
    }
});
