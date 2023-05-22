<template>
  <h1>이용권안내!</h1>

  이름:<input type="text" name="name"/> <br>
  나이:<input type="text" v-model="userAge" name="age"/> <br>
  취미:<input type="text" name="hobby"/> <br>
  <input type="submit" value="전송"/>

  <div>{{ state.items }}</div>
  <div v-for="(item, idx) in state.items" :key="idx">
    {{ idx }} : {{ item }}
  </div>

</template>
<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";

const props = defineProps({
  postId:{
    type: Object,
    required : true
  }
})

const userAge = ref({
  title : ""
});

onMounted(()=>{
  axios.get("", {

  }).then((response) => {

  }).catch((erorr) => {
    console.log(erorr)
  })
})

onMounted(()=>{
  axios.get("", {

  }).then((response) => {

  }).catch((erorr) => {
    console.log(erorr)
  })
})

onMounted(()=>{
  axios.get("", {

  }).then((response) => {

  }).catch((erorr) => {
    console.log(erorr)
  })
})
const get = () => {
  const token = Cookies.get("accessToken")
  axios.post("", {
    name : "",
    age : userAge.value.title
  },{
    headers:{
      Authorization : token
    }
  }).then((response) => {

  }).catch((erorr) => {
    console.log(erorr)
  })
}


</script>

<style scoped>

</style>