const author = document.querySelectorAll("input")[0];
const title = document.querySelectorAll("input")[1];
const content = document.querySelector("#content");
const btn = document.querySelectorAll("input")[2];




btn.addEventListener("click", (e) => {

    if(author.value.length <= 2){
        alert("Författare fält måste vara längre än 2 tecken")
        e.preventDefault();
    }
    else if(title.value.length <= 3){
        alert("Titel fält måste vara längre än 3 tecken")
        e.preventDefault();

    }
    else if(content.value.length <= 10){
        alert("Innehåll måste vara längre än 10 tecken")
        e.preventDefault();

    }

});

