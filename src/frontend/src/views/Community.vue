<template>

  <div v-if="isLoading">
    <div class="loader">
      <div class="spinner-border text-light" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>

  <div v-else>

    <div class="d-flex justify-content-center">
      <div class="boardList">

        <div>

          <div>

            <!-- 작성하기 버튼 -->
            <div>
              <button class="btn btn-info" @click="createBtn">
                <i class="fa-solid fa-pen"></i>
                작성하기
              </button>
            </div>

            <!--          <Link/>-->

            <!-- 게시글 order by -->
            <div class="d-flex justify-content-between">
              <!-- dropdown -->
              <div class="dropdown pt-3 pb-2">
                <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown"
                        :aria-expanded="isExpanded" @click="dropDownBtn">{{ selectedSort }}
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
              <!-- 검색 -->
              <div class="pt-2 pb-2">
                <nav class="navbar bg-body-tertiary">

                  <div class="container-fluid">

                    <div class="d-flex">
                      <select class="form-select me-1" style="width: 130px;" name="type" v-model="searchType">
                        <option value="all">제목+내용</option>
                        <option value="title">제목</option>
                        <option value="content">내용</option>
                        <option value="writer">작성자</option>
                      </select>
                    </div>

                    <div class="d-flex" role="search">
                      <input v-if="searchType === 'all'" class="form-control me-2" type="search" placeholder="제목+내용 검색"
                             aria-label="Search" v-model="searchKeyword" @keyup.enter="searchConditionEnter">
                      <input v-if="searchType === 'title'" class="form-control me-2" type="search" placeholder="제목 검색"
                             aria-label="Search" v-model="searchKeyword" @keyup.enter="searchConditionEnter">
                      <input v-if="searchType === 'content'" class="form-control me-2" type="search" placeholder="내용 검색"
                             aria-label="Search" v-model="searchKeyword" @keyup.enter="searchConditionEnter">
                      <input v-if="searchType === 'writer'" class="form-control me-2" type="search" placeholder="작성자 검색"
                             aria-label="Search" v-model="searchKeyword" @keyup.enter="searchConditionEnter">
                      <button class="btn btn-success" type="button" style="width: 100px;" @click="searchConditionBtn">검색
                      </button>
                    </div>

                  </div>
                </nav>
              </div>
              <!-- page -->
              <div class="pt-3 pb-2">
                <div>
                  {{ pageInfo.currentPageNumber }} / {{ pageInfo.lastPageNumber }} 페이지
                  <button class="btn btn-link" @click="prevPageBtn" :disabled="isPrevBtnDisabled"><i
                      class="fa-solid fa-arrow-left fa-xl"></i></button>
                  <button class="btn btn-link" @click="nextPageBtn" :disabled="isNextBtnDisabled"><i
                      class="fa-solid fa-arrow-right fa-xl"></i></button>
                </div>
              </div>
            </div>

            <!-- 게시글 -->
            <div>
              <table class="table table-hover">
                <thead>
                <tr>
                  <th>번호</th>
                  <th>제목</th>
                  <th>작성자</th>
                  <th><i class="fa-solid fa-binoculars"></i></th>
                  <th><i class="fa-regular fa-comment-dots"></i></th>
                  <th><i class="fa-solid fa-bolt"></i></th>
                  <th>작성일시</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="board in boardList">
                  <td>{{ board.id }}</td>
                  <td v-if="board.grade === 'NOTICE'">
                    <router-link :to="{ name: 'getBoard', params: {boardId: board.id}}">[공지사항] {{
                        board.title
                      }}
                    </router-link>
                  </td>
                  <td v-else>
                    <router-link :to="{ name: 'getBoard', params: {boardId: board.id}}">{{ board.title }}</router-link>
                  </td>
                  <td>{{ board.writer }}</td>
                  <td>{{ board.viewCount }}</td>
                  <td>{{ board.commentCount }}</td>
                  <td>{{ board.likeCount }}</td>
                  <td>{{ board.inserted }}</td>
                </tr>
                </tbody>
              </table>
            </div>

            <!-- pagination -->
            <div class="container">
              <div class="row">
                <nav aria-label="Page navigation example">
                  <ul class="pagination justify-content-center">
                    <li class="page-item">
                      <button class="page-link" aria-label="Previous" @click="navigateToPage(1)"
                              v-if="pageInfo.currentPageNumber !== 1">
                        <span aria-hidden="true">&laquo;</span>
                      </button>
                    </li>
                    <li v-for="pageNum in computedPageRange" :key="pageNum" class="page-item">
                      <button class="page-link" @click="navigateToPage(pageNum)">{{ pageNum }}</button>
                    </li>
                    <li class="page-item">
                      <button class="page-link" aria-label="Next" @click="navigateToPage(pageInfo.lastPageNumber)"
                              v-if="pageInfo.currentPageNumber !== pageInfo.lastPageNumber">
                        <span aria-hidden="true">&raquo;</span>
                      </button>
                    </li>
                  </ul>
                </nav>
              </div>
            </div>

          </div>

        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from "vue";
import axios from "axios";
import {RouterLink, useRoute, useRouter} from "vue-router";
import Cookies from "vue-cookies";
import Link from "@/components/community/container/Link.vue";

const router = useRouter();
const route = useRoute();

const isLoading = ref(true);

// 페이지 관련 정보
const pageInfo = ref({
  leftPageNumber: '',
  rightPageNumber: '',
  currentPageNumber: '',
  lastPageNumber: '',
  records: ''
});

// 페이지 번호 재할당
const computedPageRange = computed(() => {
  const rangeStart = pageInfo.value.leftPageNumber;
  const rangeEnd = pageInfo.value.rightPageNumber;
  return Array.from({length: rangeEnd - rangeStart + 1}, (_, index) => rangeStart + index);
});

// 검색 키워드 관련
const searchType = ref('all');
const searchKeyword = ref('');

// dropdown 목록
const isExpanded = ref(false);

// 게시글 목록 리스트
const boardList = ref([]);

// 목록 초기값
const selectedSort = ref('최신순');

// 목록에서 선택한 값에 맞게 반환
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

// 작성하기 버튼
const createBtn = () => {
  if (Cookies.get('accessToken')) {
    router.push('/community/new');
  } else {
    const ok = confirm("로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?");
    if (ok) {
      router.push('/login');
    } else {
      router.push('/community');
    }
  }
};

// dropdown 버튼
const dropDownBtn = () => {
  isExpanded.value = !isExpanded.value;
};

const getBoardListWithQuery = () => {
  // 게시글 순서선택
  const query = {
    type: searchType.value,
    keyword: searchKeyword.value,
    sort: selectedSortLabel.value
  };

  // 현재 URL에 쿼리 파라미터 추가 또는 변경
  router.push({query}).then(() => {
    // 목록 요청
    getBoardList();
  });
};

// 센터 검색 버튼
const searchConditionBtn = () => {
  getBoardListWithQuery();
};

// 검색 후 엔터 기능
const searchConditionEnter = () => {
  getBoardListWithQuery();
};

const selectSort = (sortValue) => {
  isExpanded.value = false;
  selectedSort.value = sortValue;

  getBoardListWithQuery();
};

// 뒤로가기가 아닌 행동을 하면 localStorage에 값들을 clear
window.addEventListener('beforeunload', () => {
  localStorage.clear();
});

const localStorageGetItem = () => {
  selectedSort.value = localStorage.getItem("selectedSort") || "최신순";
  searchType.value = localStorage.getItem("searchType") || "all";
  searchKeyword.value = localStorage.getItem("searchKeyword") || "";
};

// watch를 사용하여 선택한 값이 변경될 때마다 localStorage에 저장
watch([selectedSort, searchType, searchKeyword], () => {
  localStorage.setItem("selectedSort", selectedSort.value);
  localStorage.setItem("searchType", searchType.value);
  localStorage.setItem("searchKeyword", searchKeyword.value);
});

// 1번 페이지와 마지막 페이지일 때 disabled 기능
const isPrevBtnDisabled = computed(() => {
  return pageInfo.value.currentPageNumber === 1;
});

const isNextBtnDisabled = computed(() => {
  return pageInfo.value.currentPageNumber === pageInfo.value.lastPageNumber;
});

// 이전 페이지
const prevPageBtn = () => {
  const page = pageInfo.value.currentPageNumber - 1;
  if (page === 0) {
    alert("첫번째 페이지입니다.");
    const page = 1;
    navigateToPage(page);
  } else {
    navigateToPage(page);
  }
};

// 다음 페이지
const nextPageBtn = () => {
  const page = pageInfo.value.currentPageNumber + 1;
  if (page === pageInfo.value.lastPageNumber + 1) {
    alert("마지막 페이지입니다.");
    const page = pageInfo.value.lastPageNumber;
    navigateToPage(page);
  } else {
    navigateToPage(page);
  }
};

// 페이지 버튼
const navigateToPage = (page) => {
  // 게시글 순서선택
  const query = {
    page: page,
    type: searchType.value,
    keyword: searchKeyword.value,
    sort: selectedSortLabel.value
  };

  // 현재 URL에 쿼리 파라미터 추가 또는 변경
  router.push({query}).then(() => {
    // 목록 요청
    getBoardList();
  });
};

const getBoardList = () => {
  axios
      .get("/api/community/board", {
        params: route.query // 현재 URL의 쿼리 파라미터를 사용하여 요청
      })
      .then((response) => {
        boardList.value = response.data.list;
        pageInfo.value = response.data.pageInfo;
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
};

onMounted(() => {
  setTimeout(() => {
    isLoading.value = false;
  }, 300);

  // 뒤로가기 이벤트 감지
  window.onpopstate = function (event) {
    if (event.state == null) {
      localStorageGetItem();
      getBoardList();
    }
  };

  localStorageGetItem();
  getBoardList();
});

</script>

<style scoped>

.boardList {
  width: 100%;
  margin-top: 50px;
  background-color: white;
  border: 5px solid #ccc;
}

.loader {
  position: fixed;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

</style>