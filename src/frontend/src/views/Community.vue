<template>
  <div class="container-lg">
    <h1>게시물 목록 - 작업중!!</h1>

    <div class="d-flex justify-content-between">

      <div>
        <button class="btn btn-dark" @click="createBtn">
          작성하기
        </button>
      </div>

      <div class="dropdown">
        <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown"
                :aria-expanded="isExpanded" @click="dropDownBtn">
          최신순
        </button>
        <ul class="dropdown-menu" :class="{show: isExpanded}">
          <li>
            <button class="dropdown-item" type="button" @click="selectSort('최신순')">최신순</button>
          </li>
          <li>
            <button class="dropdown-item" type="button" @click="selectSort('추천순')">추천순</button>
          </li>
          <li>
            <button class="dropdown-item" type="button" @click="selectSort('댓글순')">댓글순</button>
          </li>
          <li>
            <button class="dropdown-item" type="button" @click="selectSort('조회순')">조회순</button>
          </li>
        </ul>
      </div>

    </div>

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
        <td>{{ board.id }}</td>
        <td>
          <RouterLink :to="{ name: 'getBoard', params: {boardId: board.id}}"> {{ board.title }}</RouterLink>
        </td>
        <td>{{ board.writer }}</td>
        <td>{{ board.inserted }}</td>
      </tr>
      </tbody>
    </table>
  </div>

</template>

<script setup>


import {computed, onMounted, ref} from "vue";
import axios from "axios";
import {RouterLink, useRouter} from "vue-router";
import Cookies from "vue-cookies";

const router = useRouter();

// 게시글 목록 리스트
const boardList = ref([]);

// 게시글 순서선택
const selectSort = (sortValue) => {
  selectedSort.value = sortValue;
  console.log(selectedSortLabel.value);

  axios.get(`/api/community?sort=${selectedSortLabel.value}`)
      .then((response) => {
        boardList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
}

const selectedSortLabel = computed(() => {
  switch (selectedSort.value) {
    case '최신순':
      return 'id';
    case '추천순':
      return 'like';
    case '댓글순':
      return 'comment';
    case '조회순':
      return 'view';
    default:
      return '';
  }
});

// dropdown 목록
const isExpanded = ref(false);

// 목록 초기값
const selectedSort = ref('최신순');

// 작성하기 버튼
const createBtn = () => {
  if (Cookies.get('accessToken')) {
    router.push('/community/new');
  } else {
    router.push('/login');
  }
}

// dropdown 버튼
const dropDownBtn = () => {
  isExpanded.value = !isExpanded.value;
}


onMounted(() => {
  axios
      .get("/api/community", {})
      .then((response) => {
        boardList.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
});

</script>

<style scoped>

</style>