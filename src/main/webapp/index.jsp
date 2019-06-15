<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>China NO.1</title>
    <script src="d3V3.js"></script>
</head>
<body>
<script>
    //画布大小
    var width = 1000;
    var height = 1000;
    var svg = d3.select("body")
        .append("svg")
        .attr("width", width)
        .attr("height", height);
    //投影
    var projection = d3.geo.mercator()
        .center([107,31])
        .scale(850)
        .translate([width/2,height/2])
    // 地理路径生成器
    var path = d3.geo.path()
        .projection(projection); // 设定路径生成器的投影函数
    //绘制地图
    //所有读取到的信息会被放到 root 中
    var color = d3.scale.category20()
    d3.json("china.geojson", function (error, root) {
        if (error) {
            return console.error(error);
        }
        console.log(root.features);
        svg.selectAll("path")
            .data( root.features )
            .enter()
            .append("path")
            .attr("stroke","#000")
            .attr("stroke-width",1)
            .attr("fill", function(d,i){
                return color(i);
            })
            .attr("d", function(d){
                return path(d);
            })
            .on("mouseover",function(d,i){
                d3.select(this)
                    .attr("fill","yellow");
            })
            .on("click",function(d,i){
                d3.select(this)
                    .attr("fill","black");
            })
            .on("mouseout",function(d,i){
                d3.select(this)
                    .attr("fill",color(i));
            });
    });
</script>
</body>
</html>
