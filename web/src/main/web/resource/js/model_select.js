function model_selct() {
    var ob = document.getElementById("model_big_select");
    var value = ob.selectedIndex;
    var model = document.getElementById("model_config");
    var show = document.getElementById("model_show");
    model.innerHTML = model.innerHTML + "<select></select>";
    show.innerHTML = value;


}