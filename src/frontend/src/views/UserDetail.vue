<template>

  <div>
    <div class="row">
      <div class="col container-lg">
        <div class="col-12 col-lg-10 centered">
          <div :class="user.trainerName == null ? 'col card container' : 'col card'">
            <h1 class="text-center">{{ user.name }}님의 프로필</h1>
            <div class="mb-3 profile">
              <div class="mb-3">
                <label for="" class="form-label">이름</label>
                <input type="text" class="form-control" :value="user.name" readonly>
              </div>
              <div v-if="user.centerName != null">
                <div class="mb-3">
                  <label for="" class="form-label">센터 이름</label>
                  <input type="text" class="form-control" :value="user.centerName" readonly>
                </div>
                <div class="mb-3">
                  <label for="" class="form-label">센터 주소</label>
                  <textarea type="text" class="form-control" :value="user.centerAddress" readonly/>
                </div>
                <div class="mb-3">
                  <label for="" class="form-label">담당 트레이너</label>
                  <input type="text" class="form-control" :value="user.trainerName" readonly>
                </div>
              </div>
              <div v-else>
                <div class="mb-3">
                  <label for="" class="form-label">센터 이름</label>
                  <br>
                  <input type="text" class="form-control" value="등록된 센터가 없습니다.">
                  <a href="/center">지점 검색</a>
                </div>
              </div>
              <div class="mb-3" v-if="user.startDate != null">
                <label for="" class="form-label">이용권 기간</label>
                <input type="text" class="form-control" :value="periodMembership" readonly>
              </div>
              <div class="mb-3" v-else>
                <label for="" class="form-label">이용권 기간</label>
                <input type="text" class="form-control" value="이용권이 없습니다." readonly>
                <a href="/membership">이용권 안내</a>
              </div>
              <div class="mb-3" v-if="user.remainingPT != null || user.remainingPT == 0">
                <label for="" class="form-label">남은 PT 횟수</label>
                <input type="text" class="form-control" :value="user.remainingPT" readonly>
              </div>
              <div class="mb-3" v-else>
                <label for="" class="form-label">남은 PT 횟수</label>
                <input type="text" class="form-control" value="PT 횟수가 없습니다." readonly>
              </div>
            </div>
            <div v-if="user.centerName !== null">
              <div class="mb-3 align-content-end profile">
                <label @click="toggleChat">
                  쪽지 기능 활성화하기
                  <i :class="toggleChatIconClass" class="fa-solid fa-2xl"></i>
                </label>
              </div>
              <div class="mb-3 align-content-end profile">
                <label @click="toggleMemo">
                  기록 기능 활성화하기
                  <i :class="toggleMemoIconClass" class="fa-solid fa-2xl"></i>
                </label>
              </div>
            </div>
          </div>
        </div>
      </div>
      <br>

      <div class="col-12 col-lg-8 col-lg-6 container-lg chatBox" v-if="toggleChatIconClass === 'fa-toggle-on'">
        <div class="col" v-if="user.trainerName != null">
          <div class="col card">
            <div v-if="authority === 1">
              <h3 class="text-center">담당 트레이너 {{ user.trainerName }}와(과)의 대화 내용</h3>
            </div>
            <div v-else>
              <h2 class="text-center">고객 {{ user.name }}와(과) 대화 내용</h2>
            </div>
            <div class="container messageBox chat-window" style="background-color: white">
              <div>
                <div>
                  <div v-for="chat in messageList" :key="chat.chatId" class="message">
                    <div>
                      <strong>{{ chat.memberName }}</strong> ({{ chat.timestamp }})
                    </div>
                    <div>{{ chat.message }}</div>
                    <br>
                  </div>
                </div>
              </div>
            </div>
            <div class="input-group">
              <input type="text" class="form-control" @keyup.enter="sendMessage" v-model="newMessage"
                     placeholder="메시지를 입력하세요"/>
              <button class="btn btn-outline-secondary" type="button" @click="sendMessage">전송</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <br>
    <div class="col-12 col-lg-10 centered" v-if="toggleMemoIconClass === 'fa-toggle-on'">
      <div class="card">
        <div>
          <button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
            <i class="fa-solid fa-pen"></i>
            작성하기
          </button>
        </div>
        <div v-if="authority === 1">
          <h1 class="text-center">기록</h1>
        </div>
        <div v-else>
          <h1 class="text-center">{{ user.name }}님의 기록</h1>
        </div>
        <div class="memo">
          <div>
            <div>
              <table class="table">
                <thead>
                <tr>
                  <th>#</th>
                  <th>구분</th>
                  <th>날짜</th>
                  <th>제목</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(memo, index) in visibleMemoList" :key="memo.memoId">
                  <td>{{ memo.memoId }}</td>
                  <td v-if="memo.division == 1">식단 관리</td>
                  <td v-else-if="memo.division == 2">운동 관리</td>
                  <td v-else>기타</td>
                  <td>{{ memo.inserted }}</td>
                  <td>
                    <a href="#" @click="getMemo(memo.memoId);" data-bs-toggle="modal"
                       data-bs-target="#staticUpdateBackdrop">
                      {{ memo.title }}
                    </a>
                  </td>
                </tr>
                </tbody>
              </table>
              <div class="text-center">
                <button v-if="visibleMemoCount < totalMemoCount" class="btn btn-primary"
                        @click="increaseVisibleMemoCount">
                  더보기
                </button>
                <button v-if="visibleMemoCount > 10" class="btn btn-primary" @click="decreaseVisibleMemoCount">
                  줄이기
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 기록 작성 모달창 -->
  <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="staticBackdropLabel">기록 작성</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            기록 구분 <br><select v-model="writeMemoDivision">
            <option disabled selected>선택해주세요</option>
            <option value="1">식단 관리</option>
            <option value="2">운동 관리</option>
            <option value="3">기타</option>
          </select>
            <div class="mb-3">
              제목 <input type="text" class="form-control" v-model="writeMemoTitle">
            </div>
          </div>
          <div class="mb-3">
            내용 <textarea type="text" class="form-control" rows="10" v-model="writeMemoContent"></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
          <button type="button" class="btn btn-primary" @click="saveMemo" data-bs-dismiss="modal">저장</button>
        </div>
      </div>
    </div>
  </div>

  <!-- 기록 수정 모달창 -->
  <div class="modal fade" id="staticUpdateBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="staticUpdateBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="staticUpdateBackdropLabel">기록 수정</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            <div class="mb-3">
              구분
              <div v-if="memo.division == 1">
                <input type="text" class="form-control" value="식단 관리" readonly>
              </div>
              <div v-else-if="memo.division == 2">
                <input type="text" class="form-control" value="운동 관리" readonly>
              </div>
              <div v-else>
                <input type="text" class="form-control" value="기타" readonly>
              </div>
            </div>
            <div class="mb-3">
              제목 <input type="text" class="form-control" v-model="memo.title">
            </div>
          </div>
          <div class="mb-3">
            내용 <textarea type="text" class="form-control" rows="10" v-model="memo.content"></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" @click="deleteMemo" data-bs-dismiss="modal">삭제</button>
          <button type="button" class="btn btn-warning" @click="updateMemo" data-bs-dismiss="modal">수정</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
        </div>
      </div>
    </div>
  </div>


</template>

<script setup>
import {computed, defineProps, nextTick, onMounted, ref} from "vue";
import axios from "axios";
import Cookies from "vue-cookies";
import {showCustomAlert} from "../main";

const isLoading = ref(true)
const newMessage = ref('');
const messageList = ref({
  chatId: '',
  memberName: '',
  trainerName: '',
  timestamp: '',
  message: ''
});
const chat = ref({
  messageFrom: '',
  messageTo: '',
  timestamp: '',
  message: ''
});

const writeMemoDivision = ref();
const writeMemoTitle = ref();
const writeMemoContent = ref();
const memo = ref({
  division: '',
  title: '',
  content: '',
  memoId: ''
});
const periodMembership = ref();
const props = defineProps({
  userId: {
    type: [Number, String],
    required: true
  },
});

const memoList = ref({}); // 전체 메모 목록
const visibleMemoCount = ref(10); // 현재 보여지는 메모 수
const totalMemoCount = computed(() => memoList.value.length); // 전체 메모 수

const visibleMemoList = computed(() => {
  return memoList.value.slice(0, visibleMemoCount.value);
});

function increaseVisibleMemoCount() {
  visibleMemoCount.value += 10;
}

function decreaseVisibleMemoCount() {
  visibleMemoCount.value = Math.max(visibleMemoCount.value - 10, 10);
}


const toggleChatIconClass = ref('fa-toggle-off');
const toggleMemoIconClass = ref('fa-toggle-off');

function toggleChat() {
  toggleChatIconClass.value = toggleChatIconClass.value === 'fa-toggle-on' ? 'fa-toggle-off' : 'fa-toggle-on';
}

function toggleMemo() {
  toggleMemoIconClass.value = toggleMemoIconClass.value === 'fa-toggle-on' ? 'fa-toggle-off' : 'fa-toggle-on';
}

const deleteMemo = () => {
  axios.post("/api/memo/deleteMemo", {}, {
    params: {
      memoId: memo.value.memoId
    }
  })
      .then((response) => {
        showCustomAlert("기록이 삭제되었습니다.")
        location.reload()
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message)
        }
      })
}

const updateMemo = () => {
  axios.post("/api/memo/updateMemo", {
    memoId: memo.value.memoId,
    title: memo.value.title,
    content: memo.value.content
  }, {})
      .then((response) => {
        showCustomAlert("기록이 수정되었습니다.")
        toggleMemoIconClass.value = 'fa-toggle-on'
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message)
        }
      });
}

const getMemo = (id) => {
  axios.get("/api/memo/getMemo", {
    params: {
      memoId: id
    }
  })
      .then((response) => {
        memo.value = response.data
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message);
        }
      })
}

const saveMemo = () => {
  axios.post("/api/memo/saveMemo", {
    division: writeMemoDivision.value,
    title: writeMemoTitle.value,
    content: writeMemoContent.value,
    memberId: user.value.id
  })
      .then((response) => {
        showCustomAlert("기록이 등록되었습니다.")
        toggleMemoIconClass.value = 'fa-toggle-on'
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message);
        }
      })
}

const sendMessage = () => {
  if (newMessage.value) {
    const token = Cookies.get("accessToken");
    axios.post("/api/chat/saveMessage", {
      memberId: user.value.id,
      trainerId: user.value.trainerId,
      message: newMessage.value
    }, {
      headers: {
        Authorization: token
      }
    })
        .then((response) => {
          newMessage.value = '';
          getMessageList()
        })
        .catch((error) => {
          if (error.response) {
            alert(error.response.data.message);
          }
        })
  }
}

const user = ref({
  id: '',
  name: '',
  email: '',
  isInCenter: '',
  trainerId: '',
  trainerName: '',
  centerName: '',
  startDate: '',
  endDate: '',
  remainingPT: '',
  authority: ''
});

const authority = ref('')

onMounted(() => {
  const token = Cookies.get("accessToken")
  axios.post("/api/user/getAuthority", {}, {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        authority.value = response.data
        console.log(authority.value)
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message)
        }
      })
})

onMounted(() => {
  const token = Cookies.get("accessToken")
  axios.get(`/api/memo/getMemoList/${props.userId}`, {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        memoList.value = response.data;
      })
      .catch((error) => {
        if (error.response.data.message) {
          alert(error.response.data.message)
        }
      })
})

const getMessageList = () => {
  const token = Cookies.get("accessToken")
  axios.get(`/api/chat/getMessageList/${props.userId}`, {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        messageList.value = response.data;
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message)
        }
      })
}

onMounted(() => {
  getMessageList()
})

onMounted(() => {
  setTimeout(() => {
    isLoading.value = false;
  }, 200);
  const token = Cookies.get("accessToken")
  axios.get(`/api/user/get/${props.userId}`, {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        user.value = response.data;
        periodMembership.value = `${user.value.startDate} ~ ${user.value.endDate}`

      }).catch((error) => {
    if (error.response) {
      alert(error.response.data.message);
    }
  });
});

</script>

<style scoped>
.chatBox {
  margin-right: 30px;
}

.centered {
  justify-content: center;
  align-items: center;
  margin-left: auto;
  margin-right: auto;
}

.profile {
  margin-right: 15px;
  margin-left: 15px;
}

th {
  border-bottom: none;
}

card {
  max-width: 300px;
}

td {
  border-bottom: none;
  padding: 5px;
}

.container {
  max-width: 800px;
  height: 500px;
}

.messageBox {
  overflow-y: auto;
}

.memo {
  overflow-y: auto;
  max-height: 500px;
  margin-left: 50px;
}

.row {
  padding: 5px;
}
</style>
