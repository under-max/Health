<template>
  <div class="container-lg">
    <h1>게시물 목록 - 작업중!!</h1>

    <table class="table table-hover">
      <thead>
      <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th>작성일시</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="board in boardList">
        <td>{{board.id}}</td>
        <td>
          <a href="/id/${board.id }"> {{ board.title}} </a>
        </td>
        <td>{{ board.writer }}</td>
        <td>{{ board.inserted }}</td>
      </tr>
      </tbody>
    </table>
  </div>

</template>

<script setup>



// 게시글 목록 리스트
import {onMounted, ref} from "vue";
import axios from "axios";

const boardList = ref([]);


onMounted(() => {
  axios
      .get("/api/community", {})
      .then((response) => {
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error)
      });
});


</script>

<style scoped>

</style>