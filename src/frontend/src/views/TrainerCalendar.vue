<template>
  <div v-if="isLoading">
    <div class="loader">
      <div class="spinner-border text-light" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>
  <div v-else>
    <div class="container card">
      <div class="container-md" style="background-color: white; align-content: center">
        <h1 class="container">일정 등록</h1>
        <select v-model="searchMember">
          <option disabled selected>전체리스트</option>
          <option v-for="user in list" :key="user.id" :value="user.id">{{ user.name }}</option>
        </select>
        <button class="btn btn-primary btn-sm" @click="searchMemberSchedule">검색</button>
        <button class="btn btn-primary btn-sm" @click="showAllSchedule">전체 일정 보기</button>
        <br>
        <br>
        <div class="d-flex justify-content-center">
          <button class="btn btn-primary btn-sm" @click="previousMonth">이전</button>
          <h2>{{ currentYearMonth }}</h2>
          <button class="btn btn-primary btn-sm" @click="nextMonth">다음</button>
        </div>

        <br>
        <table class="container">
          <thead>
          <tr>
            <th v-for="day in daysOfWeek" :key="day">{{ day }}</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="week in calendar" :key="week">
            <td v-for="day in week" :key="day.date" :class="{ 'today': day.isToday, 'selected': isSelected(day) }">
              <div>{{ day.date }}</div>
              <div class="event" v-if="day.date > 0">
                <div v-for="(w, key) in schedule" :key="key">
                  <div v-if="day.date === key + 1">
                    <div v-for="(obj, idx) in w.pop()" :key="idx">
                      <div v-if="year < currentDate.getFullYear()">

                      </div>
                      <div v-else-if="year == currentDate.getFullYear()">
                        <div v-if="month == currentDate.getMonth()">
                          <div v-if="day.date >= currentDate.getDate()">
                            <a href="#"
                               @click="updateModal(day); assignSchedule(obj.id, obj.memberId, obj.memberName, obj.pt)"
                               data-bs-toggle="modal" data-bs-target="#staticUpdateBackdrop">
                              {{ obj.memberName }}, {{ obj.pt }}
                            </a>
                          </div>
                          <div v-else>
                            <div>{{ obj.memberName }}, {{ obj.pt }}</div>
                          </div>
                        </div>
                        <div v-else-if="month > currentDate.getMonth()">
                          <a href="#"
                             @click="updateModal(day); assignSchedule(obj.id, obj.memberId, obj.memberName, obj.pt)"
                             data-bs-toggle="modal" data-bs-target="#staticUpdateBackdrop">
                            {{ obj.memberName }}, {{ obj.pt }}
                          </a>
                        </div>
                      </div>
                      <div v-else>
                        <a href="#"
                           @click="updateModal(day); assignSchedule(obj.id, obj.memberId, obj.memberName, obj.pt)"
                           data-bs-toggle="modal" data-bs-target="#staticUpdateBackdrop">
                          {{ obj.memberName }}, {{ obj.pt }}
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-if="year < currentDate.getFullYear()">

                </div>
                <div v-else-if="year == currentDate.getFullYear()">
                  <div v-if="month == currentDate.getMonth()">
                    <div v-if="day.date >= currentDate.getDate()">
                      <button @click="openModal(day)" type="button" class="btn btn-primary btn-sm"
                              data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                        등록
                      </button>
                    </div>
                  </div>
                  <div v-else-if="month > currentDate.getMonth()">
                    <button @click="openModal(day)" type="button" class="btn btn-primary btn-sm"
                            data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                      등록
                    </button>
                  </div>
                </div>
                <div v-else>
                  <button @click="openModal(day)" type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal"
                          data-bs-target="#staticBackdrop">
                    등록
                  </button>
                </div>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
         aria-labelledby="staticBackdropLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="staticBackdropLabel">일정 등록</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <h5>고객리스트</h5>
            이름 <select v-model="userSelect">
            <option disabled selected>고객리스트</option>
            <option v-for="user in list" :value="user">{{ user.name }}</option>
          </select>
            <br>
            시간 <select v-model="timeSelect">
            <option disabled selected>시간</option>
            <option v-for="time in 24" :key="time">{{ time }}</option>
          </select>
            <p>남은 PT 횟수 : {{ userSelect.remainingPT }}</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" @click="saveModal">저장</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <br>

  <!-- 일정 수정/삭제 모달 창 -->
  <div class="modal fade" id="staticUpdateBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="staticUpdateBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">일정 수정</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <h5>고객리스트</h5>
          <h6>기존 예약 : {{ memberName }}님 {{ savePtTime }}시간 </h6>
          이름: <input v-model="memberName"/>
          <br>
          시간: <select v-model="ptHour">
          <option :value="savePtTime">{{ savePtTime }}</option>
          <option v-for="time in 24" :key="time" :value="time">{{ time }}</option>
        </select>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-warning" @click="updateSchedule">수정</button>
          <button type="button" class="btn btn-danger" @click="deleteSchedule(id, memberId)">삭제</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, onMounted, defineProps} from 'vue';
import axios from "axios";
import Cookies from "vue-cookies";
import {showCustomAlert} from "../main";

// 달력 데이터 초기화
const currentDate = new Date();
const year = ref(currentDate.getFullYear());
const month = ref(currentDate.getMonth());
const day = ref(currentDate.getDay())
const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
const userSelect = ref({});
const timeSelect = ref()
const schedule = ref(new Map());
const searchScheduleMember = ref(new Map())
const isLoading = ref(true)
const events = ref([]);
const selectedDate = ref(null);
const memberId = ref('')
const memberName = ref('')
const ptHour = ref('')
const id = ref('')
const savePtTime = ref('');
const remainingPT = ref('');
// 해당 트레이너의 고객리스트 저장
const list = ref({
  id: '',
  name: '',
  remainingPT: ''
});

const props = defineProps({
  trainerId: {
    type: [Number, String],
    required: true
  },
});

const assignSchedule = (sid, mid, name, pt) => {
  id.value = sid;
  memberId.value = mid;
  memberName.value = name;
  ptHour.value = pt;
  savePtTime.value = pt;
}

const searchMember = ref({}); // null로 초기화

const showAllSchedule = () => {
  location.reload();
}

const searchMemberSchedule = () => {
  const token = Cookies.get('accessToken');
  if (searchMember.value) { // 유효한 선택 확인
    axios.get("/api/schedule/searchMemberSchedule", {
      headers: {
        Authorization: token
      },
      params: {
        memberId: searchMember.value,
        month: month.value + 1,
        year: year.value
      }
    })
        .then((response) => {
          const responseData = response.data;
          schedule.value = new Map(Object.entries(responseData));
          console.log(searchScheduleMember.value);
        })
        .catch((error) => {
          if (error.response) {
            alert(error.response.data.message);
          }
        });
  }
};


const changeMonth = () => {
  setTimeout(() => {
    isLoading.value = false;
  }, 600)
  const token = Cookies.get('accessToken')

  axios.get("/api/schedule/getList", {
    headers: {
      Authorization: token
    },
    params: {
      month: month.value + 1,
      year: year.value
    }
  })
      .then((response) => {
        const responseData = response.data;
        schedule.value = new Map(Object.entries(responseData));
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message);
        }
      })
}

// 이전 월로 이동
const previousMonth = () => {
  if (month.value === 0) {
    year.value--;
    month.value = 11;
  } else {
    month.value--;
  }
  changeMonth();
};

// 다음 월로 이동
const nextMonth = () => {
  if (month.value === 11) {
    year.value++;
    month.value = 0;
  } else {
    month.value++;
  }
  changeMonth()
};

// 일정 선택 여부 확인
const isSelected = day => {
  const selectedDate = new Date(year.value, month.value, day.date);
  return events.value.some(event => event.date.toDateString() === selectedDate.toDateString());
};

// x 버튼 클릭시 스케줄 삭제
const deleteSchedule = (id, memberId) => {
  axios.post("/api/schedule/deleteSchedule", {
    id: id,
    memberId: memberId
  })
      .then((response) => {
        showCustomAlert("스케줄이 삭제되었습니다.")
        location.reload();
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message);
        }
      })
};

const setTime = (time) => {
  let str = ':00'
  if (time > 9) {
    time = time + str;
  } else time = '0' + time + str;

  return time;
}

// 수정 버튼 누르면 수정된 값 업데이트 처리(POST)
// 스케줄 아이디를 넘겨서 해당 스케줄 아이디의 PT시간 변경
const updateSchedule = () => {

  let time = new Date(selectedDate.value)
  time.setHours(ptHour.value);

  axios.post("/api/schedule/updateSchedule", {
    id: id.value, // 스케줄 아이디
    memberId: memberId.value, //
    ptHour: time
  })
      .then((response) => {
        showCustomAlert("수정 되었습니다.")
        location.reload();
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message);
        }
      })
}

// 클릭된 스케줄id, 이름, PT시간, remainingPT 불러오기(GET)
const updateModal = (day) => {
  // 선택된 일정 수정
  selectedDate.value = new Date(year.value, month.value, day.date);
};

const getUserList = () => {
  axios.post(`/api/responsibleUserList/${props.trainerId}`, {})
      .then((response) => {
        list.value = response.data;
      })
      .catch((error) => {
        if (error.response) {
          alert(error.response.data.message);
        }
      })
}

// 일정등록 버튼 누르면 모달창 오픈
// 모달창 내 해당 트레이너의 고객리스트(이름, PT시간, 남은 PT횟수) 선택 및 조회
const openModal = (day) => {
  getUserList()
  selectedDate.value = new Date(year.value, month.value, day.date);
};



// 일정 등록 모달창에서 고객, PT시간 선택하면 선택한 값들이 POST 방식으로 controller->service->mapper->DB 저장
const saveModal = () => {
  // 선택된 날짜에 일정 추가
  let time = new Date(selectedDate.value)
  time.setHours(timeSelect.value);

  axios.post("/api/schedule/saveSchedule", {
    memberId: userSelect.value.id,
    ptTime: time
  })
      .then(() => {
        showCustomAlert("등록 되었습니다.")
        location.reload();
      })
      .catch((error) => {
        alert(error.response.data.message);
      });
};

// 현재 월 계산
const currentYearMonth = computed(() => {
  const date = new Date(year.value, month.value);
  return `${date.getFullYear()}년 ${date.getMonth() + 1}월`;
});

// 달력 생성
const calendar = computed(() => {
  const firstDayOfMonth = new Date(year.value, month.value, 1).getDay();
  const daysInMonth = new Date(year.value, month.value + 1, 0).getDate();
  const days = [];
  let day = 1;
  for (let i = 0; i < 6; i++) {
    const week = [];
    for (let j = 0; j < 7; j++) {
      if (i === 0 && j < firstDayOfMonth) {
        week.push({date: '', isToday: false, events: []});
      } else if (day <= daysInMonth) {
        const currentDate = new Date(year.value, month.value, day);
        const isToday = currentDate.toDateString() === new Date().toDateString();
        const eventsOnDay = events.value.filter(event => event.date.toDateString() === currentDate.toDateString());
        week.push({date: day, isToday, events: eventsOnDay});
        day++;
      } else {
        week.push({date: '', isToday: false, events: []});
      }
    }
    days.push(week);
    if (day > daysInMonth) break;
  }
  return days;
});

onMounted(() => {
  getUserList()
  changeMonth()
});
</script>

<style scoped>
.loader {
  position: fixed;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

li {
  color: white;
}

button {
  margin: 0 5px;
  cursor: pointer;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  text-align: center;
  padding: 6px;
  border: 1px solid #ccc;
}

.today {
  background-color: #e8f0fe;
  font-weight: bold;
}

.selected {
  background-color: #d9e6fc;
}

.event {
  margin-top: 5px;
}

input[type='text'] {
  width: 100%;
  padding: 5px;
}

.modal-content {
  width: 600px;
  max-height: 400px;
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.modal-title {
  font-size: 20px;
  font-weight: bold;
}

.modal-body {
  flex-grow: 1;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  gap: 10px;
}

.modal-footer button {
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}

.modal-footer button.btn-secondary {
  background-color: #ccc;
  color: #fff;
  border: none;
}

.modal-footer button.btn-primary {
  background-color: #007bff;
  color: #fff;
  border: none;
}

a {
  color: #007bff;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>