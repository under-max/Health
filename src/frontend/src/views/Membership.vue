<template>
  <h1>이용권안내!</h1>

  아이디<input type="text" v-model="memberId"/> <br>
  횟수<input type="text" v-model="remainingPT"/> <br>
  <input class="btn btn-primary" type="submit" @click="submit" value="전송"/>

  <div v-for="membership in memberships">
    {{ membership.membershipId }}
    {{ membership.memberId }}
    {{ membership.startDate }}
    {{ membership.endDate }}
    {{ membership.remainingPT }}
  </div>


</template>
<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";


const memberId = ref();
const remainingPT = ref([]);
const memberships = ref();

onMounted(() => {
  axios.get("/api/test/memberships", {}).then((response) => {
    console.log(response.data);
    memberships.value = response.data;
  }).catch((error) => {
    console.log(error)
  });
});
const submit = () => {
  axios.post("/api/test/create", {
    memberId: memberId.value,
    remainingPT: remainingPT.value
  }).then((response) => {
    console.log(response.data)
  }).catch((error) => {
    console.log(error)
  })
}

// const get = () => {
//   const token = Cookies.get("accessToken")
//   axios.post("", {
//     name: "",
//     age: userAge.value.title
//   }, {
//     headers: {
//       Authorization: token
//     }
//   }).then((response) => {
//
//   }).catch((erorr) => {
//     console.log(erorr)
//   })
// }


</script>

<style scoped>

</style>