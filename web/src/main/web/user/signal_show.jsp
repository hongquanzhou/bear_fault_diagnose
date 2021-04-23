<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.util.*"%>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="bean.signal_path" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"%>
<html>
<head>
    <title>signal_show</title>
</head>
<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script>
<script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/echarts/5.0.2/echarts.common.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="../resource/js/signal_show.js"></script>
<script >
    function ceil(x) {
        let ret = 0;
        let p = [64,128,256,512,1024,2048,4096,8192];
        while(x>0)
        {
            for(let i=0;i<p.length;i++)
            {
                if(p[i]>x/2)
                {
                    x = x-p[i];
                    ret += p[i];
                    break;
                }
            }
        }
        return ret;
    }
    function  preProcess(input) {
        let len = input.length;
        let str1 = len + "";
        if(str1.length<5) return;
        str2 = str1.substr(0,4);
        len = parseInt(str2);
        len1 = len + 1;
        len2 = ceil(len1);
        for(let i=4;i<str1.length;i++)
        {
            len2 = len2*10;
        }
        for(let i=input.length;i<len2;i++)
        {
            input.push(0);
        }
    }
    /*  下载完后触发FFT  */
    let count;
    function minus() {
        count = count - 1;
        if(count==0)
        {
            transform();
        }
    }
    var dss = [];
    function transform() {
        for(let i=0;i<ds.length;i++)
        {
            preProcess(ds[i]);
            const data = new ComplexArray(ds[i]);
            data.FFT();
            let freq = data.magnitude();
            dss[i] = [];
            let delta = 10000/freq.length;
            let start = -5000;
            for(let j=0;j<freq.length;j++)
            {
                dss[i].push([start,freq[j]]);
                start = start + delta;
            }
        }
    }
    function drawFreqZones() {
        $("#show_title")[0].innerHTML = "频域";
        allData.splice(0,allData.length);
        selected = getSelectedChannel();
        let temp = [0,0,0,0,0,0,0,0,0];
        for(let i=0;i<selected.length;i++)
        {
            allData.push(selected[i]);
        }

        setTimeout(delayDrawFreq,5);
    }
    function delayDrawFreq() {
        for(let i=0;i<selected.length;i++)
        {
            drawFreqZone(i);
        }
    }
    function drawFreqZone(a) {
        let i = selected[a];
        let chartDom = $(".chart")[a];
        let TimeChart = echarts.init(chartDom);
        var option;
        option = {
            tooltip: {
                trigger: 'axis',
                position: function (pt) {
                    return [pt[0], '10%'];
                }
            },
            title: {
                left: 'center',
                text: signals[parseInt(i/4)].key + " 通道:"+i%4,
            },
            toolbox: {
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    restore: {},
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'value',
                boundaryGap: false,
                name:"采样点",
                nameLocation: "middle",
                nameGap:20
            },

            yAxis: {
                show:true,
                name:"振动加速度(m/s^2)",
                nameLocation:"end",
                type: 'value',
                boundaryGap: [0, '100%'],
                // min: function (value) {
                //     return -1000;
                // },
                // max: function (value) {
                //     return 1000;
                // }
            },
            dataZoom: [{
                type: 'inside',
                start: 0,
                end: 20
            }, {
                start: 0,
                end: 20
            }],
            series: [
                {
                    name: '通道一',
                    type: 'line',
                    smooth: true,
                    symbol: 'none',
                    // areaStyle: {},
                    data: dss[i]
                }
            ]
        };
        option && TimeChart.setOption(option);
        window.addEventListener("resize", function() {
            TimeChart.resize();
        });
    }
</script>
<style>
    #show_title{
        text-align: center;
    }
</style>
<script>
    var allData = [];
    var featureNames = ["均值","绝对平均值","方差","标准差","均方根","方根幅值","最小值","最大值","峭度"];
    var featureValues = [];
</script>
<body>
    <div id="nav">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item">
                        <a class="nav-link " href="welcome.jsp">首页</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="signal_analysis.html">数据分析<span class="sr-only">(current)</span></a>
                    </li>
                    <li class="nav-item ">
                        <a class="nav-link " href="model_construct_AlexNet.html">模型构建与下载</a>
                    </li>
                    <li class="nav-item ">
                        <a class="nav-link" href="statistic_information.html">统计信息</a>
                    </li>
                </ul>
                <h5 style="height: 10px;" align="right"><a id="username" class="user" onmouseenter="showUserMenu(event)"></a> &nbsp;</h5>
            </div>
        </nav>
    </div>
    <div id="userMenu" style="position: fixed;width: 90px;height:160px;display: none;"onmouseleave="disShowUserMenu(event)">
        <div style="margin: 50px 20px 10px 10px;border-radius: 10px;box-shadow: darkgrey 10px 10px 30px 5px;width: 90px;height:100px;position: relative">
            <ul style="list-style-type: none;position: absolute;margin-top: 15px;">
                <li><a href="userHome.html" class="menuli">个人中心</a></li>
                <li><a href="userSetting.html" class="menuli">账号设置</a></li>
                <li><a href="login.jsp" class="menuli" >退出</a></li>
            </ul>
        </div>
        <style>
            .menuli{
                margin-left: 10px;
            }
        </style>
    </div>
    <script>
        function showUserMenu(e) {
            at = $("#username")[0].getBoundingClientRect();
            left = at.left;
            style = $("#userMenu")[0].style;
            style.display=""
            style.left = left
            style.top = 0;
        }
        function disShowUserMenu(e) {
            $("#userMenu")[0].style.display="none"
        }
    </script>
    <div style="width: 24%;height:720px;float:left;">
        <div style="height: 500px;width:100%;overflow: auto;margin-top: 10px;margin-left: 10px">
            <table id="select_signal">
                <%
                    request.setCharacterEncoding("utf-8");
                    BufferedReader br = new BufferedReader(new InputStreamReader((request.getInputStream())));
                    String line = null;
                    int count = 0;
                    Gson gson = new Gson();
                    signal_path s = null;
                   // out.print("<tr><th>信号</th><th>选中</th><th>状态</th></tr>");
                    while((line = br.readLine())!=null)
                    {
                        if(count%4==3) {
                            out.print("<tr>");
                            s = gson.fromJson(line, signal_path.class);
                            out.print("<th>"+s.getString()+"</th>"+"<th><input type='checkbox'><input type='checkbox'><input type='checkbox'><input type='checkbox'></th>");
                            out.print("<th style='display: none'>"+s.getPath()+"</th>"+"<th><p style=\"color:red;margin-top:10px\">downloading</p></th>");
                            out.print("</tr>");
                        }
                        count++;
                    }
                %>
            </table>
        </div>
        <div style="margin-left: 30px;margin-top: 10px">
            <input type="button" id="time_zone" onclick="drawTimeZones()" value="时域"/>
            <input type="button" id="frequence_zone" onclick="drawFreqZones()" value="频域"/>
        </div>
    </div>
    <div style="width: 76%;height:900px;float: left;margin-top: 10px;border-left: black solid 2px;">
        <h3 id="show_title"></h3>
        <div id="graph_container" style="width: 100%;height:900px;overflow:auto;float: left;text-align: center">
            <div style="width: 100%;height: 300px;border-bottom:black 2px solid;display: flex;" v-for = "(item1,index1) in items">
                <div class="chart" style="width: 80%;height:300px;margin-top: 10px"  >
                </div>
                <div style="width: 20%;height: 300px;margin-top: 10px">
                    <p v-for="(item2,index2) in featureNames" style="line-height: 8px;font-size: 8px">{{item2}}:{{featureValues[item1][index2]}}</p>
                </div>
            </div>
        </div>
    </div>
    <script>
        function  CalTimeZone()
        {
            var m={};
            m.Mean = function (data) {
                let sum=0;
                for(var i=0;i<data.length;i++)
                {
                    sum += data[i];
                }
                return (sum/data.length).toFixed(2);
            }
            m.AbsMean = function (data) {
                let sum = 0;
                for(var i=0;i<data.length;i++)
                {
                    sum += Math.abs(data[i]);
                }
                return (sum/data.length).toFixed(2);
            }
            m.variance = function (data) {
                let sum = 0;
                for(var i=0;i<data.length;i++)
                {
                    sum += Math.pow(data[i],2);
                }
                return (sum/data.length).toFixed(2);
            }
            m.maxValue = function (data) {
                if(data.length==0) return 0;
                let ret = -5000;
                for(var i=0;i<data.length;i++)
                {
                    if(ret<data[i])
                        ret = data[i];
                }
                return ret.toFixed(2);
            }
            m.minValue = function (data) {
                if(data.length==0) return 0;
                let ret = 5000;
                for(var i=0;i<data.length;i++)
                {
                    if(ret>data[i])
                        ret = data[i];
                }
                return ret.toFixed(2);
            }
            m.Std = function (data) {
                var mean = m.Mean(data);
                let sum = 0;
                for(var i=0;i<data.length;i++)
                {
                    sum += Math.pow(data[i]-mean,2);
                }
                return (Math.sqrt(sum/(data.length-1))).toFixed(2);
            }
            m.Rms = function (data) {
                return (Math.sqrt(m.variance(data))).toFixed(2);
            }
            m.Sra = function (data) { //方根幅值
                let sum = 0;
                for(var i=0;i<data.length;i++)
                {
                    sum += Math.sqrt(Math.abs(data[i]));
                }
                return (Math.pow(sum/data.length,2)).toFixed(2);
            }
            m.Kurtosis = function (data) {
                let mean = m.Mean(data);
                let sum =0 ;
                for(let i=0;i<data.length;i++)
                {
                    sum+=Math.pow(data[i]-mean,4);
                }
                let Std = m.Std(data);
                return (sum/Math.pow(Std,4)/(data.length-1)).toFixed(2);
            }
            m.allMethod = [];
            m.allMethod.push(m.Mean);
            m.allMethod.push(m.AbsMean);
            m.allMethod.push(m.variance);
            m.allMethod.push(m.Std);
            m.allMethod.push(m.Rms);
            m.allMethod.push(m.Sra);
            m.allMethod.push(m.minValue);
            m.allMethod.push(m.maxValue);
            m.allMethod.push(m.Kurtosis);
            m.allMethodName = [];
            m.allMethodName.push("均值");
            m.allMethodName.push("绝对平均值");
            m.allMethodName.push("方差");
            m.allMethodName.push("标准差");
            m.allMethodName.push("均方根");
            m.allMethodName.push("方根幅值");
            m.allMethodName.push("最小值");
            m.allMethodName.push("最大值");
            m.allMethodName.push("峭度");
            return m;
        }

        function  getSelectedChannel() {
            let selectedList=[];
            let k = 0;
            for(var i=0;i<signals.length;i++)
            {
                for(let j=0;j<4;j++)
                {
                    if($("#select_signal").find("tr").eq(i).find("th").eq(1).find("input").eq(j)[0].checked)
                    {
                        selectedList.push(k);
                    }
                    k++;
                }
            }
            return selectedList;
        }
        var selected;

        function drawTimeZones() {
            $("#show_title")[0].innerHTML = "时域";
            allData.splice(0,allData.length);
            selected = getSelectedChannel();
            let temp = [0,0,0,0,0,0,0,0,0];
            for(let i=0;i<selected.length;i++)
            {
                allData.push(selected[i]);
            }
            setTimeout(delayDraw,5);
        }
        function delayDraw() {
            for(let i=0;i<selected.length;i++)
            {
                drawTimeZone(i);
            }
        }
        var datas;
        var ds = [];
        var freqs;
        function drawTimeZone(a)
        {
            let i = selected[a];
            let chartDom = $(".chart")[a];
            let TimeChart = echarts.init(chartDom);
            var option;
            option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt) {
                        return [pt[0], '10%'];
                    }
                },
                title: {
                    left: 'center',
                    text: signals[parseInt(i/4)].key + " 通道:"+i%4,
                },
                toolbox: {
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'value',
                    boundaryGap: false,
                    name:"采样点",
                    nameLocation: "middle",
                    nameGap:20
                },

                yAxis: {
                    show:true,
                    name:"振动加速度(m/s^2)",
                    nameLocation:"end",
                    type: 'value',
                    boundaryGap: [0, '100%'],
                    // min: function (value) {
                    //     return -1000;
                    // },
                    // max: function (value) {
                    //     return 1000;
                    // }
                },
                dataZoom: [{
                    type: 'inside',
                    start: 0,
                    end: 20
                }, {
                    start: 0,
                    end: 20
                }],
                series: [
                    {
                        name: '通道一',
                        type: 'line',
                        smooth: true,
                        symbol: 'none',
                        // areaStyle: {},
                        data: datas[i]
                    }
                ]
            };
            option && TimeChart.setOption(option);
            window.addEventListener("resize", function() {
                TimeChart.resize();
            });
        }

    </script>
</body>
<script>
    var signalList = new Array();
    window.onload = function (ev) {
        getUser();
        getSignalDataName();
        downloadData();
        var app = new Vue(
            {
                el: "#graph_container",
                data:{
                    items:allData,
                    featureNames:featureNames,
                    featureValues:featureValues,
                }
            }
        );
    };
    function getUser() {
        var user = document.getElementById("username");
        var xhr = new XMLHttpRequest();
        xhr.open("POST","../GetUserName",true);
        xhr.send();
        xhr.onreadystatechange = function (ev1) {
            if(xhr.readyState==4&&xhr.status==200)
            {
                var content = xhr.responseText;
                if(content=="no,relogin")
                {
                    if(!alert("用户信息失效，重新登录！"))
                    {
                        window.location.href="login.jsp";
                    }
                }
                else
                {
                    user.innerText = content;
                    user.href = "userSetting.html.html";
                }
            }
        }
    }
    function getSignalDataName() {
        var table = document.getElementById("select_signal");
        var trs = table.getElementsByTagName('tr');
        for(var i=0;i<trs.length;i++)
        {
            var object = new Object();
            var tds=trs[i].getElementsByTagName("th");
            object.string = tds[0].innerHTML;
            object.path = tds[2].innerHTML;
            signalList.push(object);
        }
    }
    var signals = [];
    function downloadData() {
        datas = [];
        count = signalList.length;
        for(var i=0;i<signalList.length;i++)
        {
            var temp={};
            temp.key = signalList[i].string;
            temp.path = signalList[i].path;
            temp.data = null;
            temp.status = 0;
            signals.push(temp);
            for(let j=0;j<4;j++)
            {
                featureValues.push([0,0,0,0,0,0,0,0,0]);
            }
        }
        for(var i=0;i<signalList.length;i++)
        {
            var xhr = new XMLHttpRequest();
            xhr.open("POST","../DownLoadSignalData",true);
            xhr.send(JSON.stringify(signalList[i]));
            xhr.responseType = "arraybuffer";
            xhr.privateInfo = {index:i};
            xhr.onreadystatechange = function (ev) {
                let temp = ev.currentTarget;
                if(temp.readyState==4&&temp.status==200)
                {
                    let index = ev.currentTarget.privateInfo.index;
                    //处理数据
                    let  data1 = ev.currentTarget.response;
                    let d = [[],[],[],[]];
                    let data = [[],[],[],[]];
                    if (data1) {
                        const view1 = new DataView(data1);
                        let len = view1.byteLength/2;
                        let j = 0;
                        for(var k=0;k<len;k++)
                        {
                            let mod = k%8;
                            let times = Math.floor(k/8);
                            if(mod<4)
                            {
                                let temp1 = view1.getInt16(k*2,false)*5.0*1000/32768.0;
                                d[mod].push(temp1);
                                data[mod].push([times*0.0001,temp1]);
                            }
                        }
                        for(let k=0;k<4;k++)
                        {
                            datas[index*4+k] = data[k];
                            ds[index*4+k] = d[k];
                        }
                    }
                    let Cal = new CalTimeZone();
                    for(let k=0;k<4;k++)
                    {
                        for(var j=0;j<Cal.allMethod.length;j++)
                        {
                            featureValues[index*4+k][j] = Cal.allMethod[j](d[k]);
                        }
                    }
                    //改状态
                    signals[index].status = 1;
                    var $status = $("#select_signal>").find("tr").eq(index).find("th").eq(3)[0];
                    $status.innerHTML ="<p style='color:green;margin-top:10px'>downloaded</p>";
                    //触发转换
                    minus();
                }
            }
        }
    }
</script>
</html>
