<template>
  <h1>운동관리!</h1>
  <table class="table">
    <thead>
    <tr>
      <th>#</th>
      <th>이름</th>
      <!--            <th>주소</th>-->
      <!--            <th>전화번호</th>-->
      <!--            <th>성별</th>-->
      <!--            <th>나이</th>-->
    </tr>
    </thead>
    <tbody>
    <tr v-for="list in lists" :key="list.id">
      <td>{{ list.id }}</td>
      <td>
        <router-link :to="{ name: 'userDetail', params: { userId: list.id } }">
          {{ list.name }}
        </router-link>

      </td>
    </tr>

    </tbody>

  </table>


</template>

<script setup>
import {onMounted, ref} from 'vue'
import axios from "axios";

const id = ref();
const name = ref();
const lists = ref([])

onMounted(() => {
  axios.get("/api/test/lists", {})
      .then((response) => {
    console.log(response.data);
        lists.value = response.data
  }).catch((error) => {
    console.log(error)
  });
});

</script>

<style>
.table{
  color : white;
}

h1 {
  color : white;
}
</style>