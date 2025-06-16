console.log("This is script file")

const toggleSideBar = () => {

    if($(".sidebar").is(":visible")){
        //true
        //banda karyacha
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");
    }
    else
    {
        //false
        //chalu karayacha
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
    }
};