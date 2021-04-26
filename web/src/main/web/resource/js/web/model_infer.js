function showUserMenu(e) {
    style = $("#userMenu")[0].style;
    style.display="";
    style.right=0;
    style.top = 0;
}
function disShowUserMenu(e) {
    $("#userMenu")[0].style.display="none"
}
function test(e){
    var value = e.target.value;
    value = value.replace( /\D+/, "");
    if(value.length > 0){
        if(value.length > 1 && value[0] == 0){
            e.target.value = value.substring(1, value.length);
            return;
        }
        e.target.value = value;
    }else{
        e.target.value = 0;
    };
}

var model_list = null;
function getUser(ev) {
    let user = document.getElementById("username");
    let xhr = new XMLHttpRequest();
    xhr.open("POST","../GetUserName",true);
    xhr.send();
    xhr.onreadystatechange = function (ev1) {
        let temp = ev1.currentTarget;
        if(xhr.readyState==4&&xhr.status==200)
        {
            let content = temp.responseText;
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
};
function getModel()
{
    let model_over = document.getElementById("select_over_model");
    let model = document.getElementById("select_model");
    let model_str = "";
    let xhr = new XMLHttpRequest();
    xhr.open("post","../QueryModel",true);
    xhr.send();
    xhr.onreadystatechange = function (ev1) {
        let temp = ev1.currentTarget;
        if ((xhr.readyState == 4) && (xhr.status) == 200) {
            let content = temp.responseText;
            let str = JSON.parse(content);
            model_list = str;
            let obj = new Object();
            obj.name = "";
            obj.path = "";
            for(var i=0;i<str.length;i++)
            {
                obj = str[i];
                model_str += "<option value=\"";
                model_str += i + "\">";
                model_str += obj.name;
                model_str += "</option>";
            }
            model.innerHTML = model_str;
            // model_over.innerHTML = model_str;
        } else {
        }
    };
}
var data_source_list = null;
function getDataSource()
{
    let ds = $("#select_data_source")[0];
    let xhr = new XMLHttpRequest();
    xhr.open("post","../QueryDataSource",true);
    xhr.send();
    xhr.onreadystatechange = function (ev1) {
        let temp = ev1.currentTarget;
        if ((xhr.readyState == 4) && (xhr.status) == 200) {
            let content = temp.responseText;
            let str = JSON.parse(content);
            data_source_list = str;
            let obj = new Object();
            obj.name = "";
            obj.path = "";
            data_source_str = "";
            for(var i=0;i<str.length;i++)
            {
                obj = str[i];
                data_source_str += "<option value=\"";
                data_source_str += i + "\">";
                data_source_str += obj.name;
                data_source_str += "</option>";
            }
            ds.innerHTML = data_source_str;
        }
    };
}
var pre_parameter_list=[];
task_list = [];
function getTaskList() {
    let body = new Object();
    body.status = "success";
    body.modelName = model_list[$("#select_model")[0].value].name;
    body.sourceName = data_source_list[$("#select_data_source")[0].value].name;
    let xhr = new XMLHttpRequest();
    xhr.open("POST","../QueryTaskInfo","true");
    xhr.send(JSON.stringify(body));
    xhr.onreadystatechange = function (ev1) {
        let temp = ev1.currentTarget;
        if(temp.readyState==4&&temp.status==200)
        {
            let select = $("#select_task")[0];
            task_info = JSON.parse(temp.response);
            var str = "";
            task_list = [];
            if(task_info.length!=0)
            {
                for(let i=0;i<task_info.length;i++)
                {
                    let ob = {};
                    ob.taskName = task_info[i].taskName;
                    ob.acc = task_info[i].testAcc;
                    task_list.push(ob)
                }
                for(let i=0;i<task_list.length;i++)
                {
                    str+="<option value=" +i+">"+  task_list[i].taskName +"</option>";
                }
            }
            select.innerHTML = str;
        }
    }
}
function getAcc() {
    let val = $("#select_task")[0].value;
    $("#testAcc")[0].value = task_list[val].acc + "%";
}
function getModelHistory()
{
    let body = new Object();
    body.modelName = model_list[$("#select_model")[0].value].name;
    body.sourceName = data_source_list[$("#select_data_source")[0].value].name;
    let xhr = new XMLHttpRequest();
    xhr.open("POST","../QueryTaskInfo","true");
    xhr.send(JSON.stringify(body));
    xhr.onreadystatechange = function (ev1) {
        let temp = ev1.currentTarget;
        if(temp.readyState==4&&temp.status==200)
        {
            let select = $("#select_model_history")[0];
            task_info = JSON.parse(temp.response);
            var str = "";
            pre_parameter_list = [];
            if(task_info.length!=0)
            {
                //数组去重
                for(let i=0;i<task_info.length;i++)
                {
                    let ob =  task_info[i].preParameter;
                    pre_parameter_list.push(ob);
                }
                pre_parameter_list = Array.from(new Set(pre_parameter_list));
                for(let i=0;i<pre_parameter_list.length;i++)
                {
                    str+="<option value=" +i+">"+  pre_parameter_list[i] +"</option>";
                }
            }
            select.innerHTML = str;
        }
    }
}

window.onload = function (ev){
    getUser();
    getModel();
    getDataSource();
    $("#select_data_source").click(getModelHistory);
    $("#select_model_history").click(getTaskList);
    $("#select_task").click(getAcc);
}