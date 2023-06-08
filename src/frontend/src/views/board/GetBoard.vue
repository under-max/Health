<template>

  <div class="container-lg">

    <div class="mb-3">
      <label for="" class="form-label">제목</label>
      <input type="text" class="form-control" :value="board.title" readonly/>
    </div>

    <div class="d-flex justify-content-sm-start">
      <div class="mb-3">
        <label for="" class="form-label">작성자</label>
        <input type="text" class="form-control" :value="board.writer" readonly/>
      </div>

      <div class="mb-3">
        <label for="" class="form-label">작성일시</label>
        <input type="text" readonly class="form-control" :value="board.inserted"/>
      </div>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">본문</label>
      <textarea class="form-control" readonly rows="10">{{board.content}}</textarea>
    </div>

    <div class="justify-content-end">
      <div class="mb-3">
        <button type="button" class="btn btn-primary" @click="likeUpBtn"><i class="fa-solid fa-chevron-up"></i>
        </button>
        <button type="button" class="btn btn-light">{{ board.likeCount }}</button>
        <button type="button" class="btn btn-danger" @click="likeDownBtn"><i class="fa-solid fa-chevron-down"></i>
        </button>
      </div>
    </div>

  </div>

</template>

<script setup>

import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";
import router from "@/router";

const props = defineProps({
  boardId: {
    type: [Number, String],
    required: true
  },
});

const board = ref({
  title: '',
  content: '',
  writer: '',
  likeCount: '',
  inserted: ''
})

const likeUpBtn = () => {
  const token = Cookies.get('accessToken');

  axios
      .patch(`/api/community/board/${props.boardId}/likeUp`, {}, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        console.log(response.data);
        board.value.likeCount = response.data;
      })
      .catch((error) => {
        console.log(error);
      });
}

const likeDownBtn = () => {
  const token = Cookies.get('accessToken');

  axios
      .patch(`/api/community/board/${props.boardId}/likeDown`, {}, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        board.value.likeCount = response.data;
      })
      .catch((error) => {
        console.log(error);
      });
}

onMounted(() => {
  axios
      .get(`/api/community/board/${props.boardId}`, {})
      .then((response) => {
        console.log(response.data);
        board.value = response.data;
      })
      .catch((error) => {
        console.log(error)
        router.replace("/community")
        alert(error.response.data.message);
      });
});
</script>

<style scoped>

.container-lg {
  margin-top: 100px;
}

</style>