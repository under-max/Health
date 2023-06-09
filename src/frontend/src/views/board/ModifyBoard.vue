<template>

  <div class="container-lg">

    <div class="d-flex justify-content-between">

      <div id="info">
        <span>{{ board.writer }}</span>
      </div>

      <div>
        <button type="button" class="btn btn-secondary" @click="modifyBtn">수정</button>
        <button type="button" class="btn btn-danger" @click="cancelBtn">취소</button>
      </div>

    </div>

    <div class="mb-3">
      <label for="" class="form-label">제목</label>
      <input type="text" class="form-control" v-model="board.title"/>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">본문</label>
      <textarea class="form-control" rows="10" v-model="board.content">{{board.content}}</textarea>
    </div>

    <div class="d-flex justify-content-end">

    </div>


  </div>

</template>

<script setup>

import {defineProps, onMounted, ref} from "vue";
import router from "@/router";
import axios from "axios";
import Cookies from "vue-cookies";

const props = defineProps({
  boardId: {
    type: [Number, String],
    required: true
  }
});

const board = ref({
  title: '',
  content: '',
  writer: '',
  inserted: ''
});

const modifyBtn = () => {
  const token = Cookies.get('accessToken');

  axios
      .patch(`/api/community/board/${props.boardId}`, {
        title: board.value.title,
        content: board.value.content
      }, {
        headers: {
          Authorization: token
        }
      })
      .then((response) => {
        if (response.data) {
          alert(response.data);
          router.replace(`/community/board/${props.boardId}`);
        }
      })
      .catch((error) => {
        if (error.response.status === 400) {
          alert(error.response.data);
        } else if (error.response.data.status === 500) {
          alert(error.response.data.message);
        }
        router.push(router.currentRoute.value.fullPath);
      });
};

const cancelBtn = () => {
  router.replace(`/community/board/${props.boardId}`)
};

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

#info {
  color: white;
}

</style>