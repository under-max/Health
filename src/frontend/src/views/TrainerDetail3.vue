<template>
  <div>
    <h2>Calendar Form</h2>
    <div>
      <label for="year">Year:</label>
      <input type="number" id="year" v-model="year" />
    </div>
    <div>
      <label for="month">Month:</label>
      <input type="number" id="month" v-model="month" min="1" max="12" />
    </div>
    <div>
      <button @click="previousMonth">이전 달</button>
      <button @click="nextMonth">다음 달</button>
    </div>
    <h3>{{ currentMonth }}</h3>
    <table>
      <thead>
      <tr>
        <th v-for="dayOfWeek in daysOfWeek" :key="dayOfWeek">{{ dayOfWeek }}</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(week, index) in calendar" :key="index">
        <td v-for="day in week" :key="day.date">
          <button
              v-if="day.date"
              :class="{ today: day.isToday, selected: isSelected(day) }"
              @click="selectDate(day)"
          >
            {{ day.date }}
          </button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>

 
</template>

<script setup>
import { ref, computed } from 'vue';

const currentDate = new Date();
const year = ref(currentDate.getFullYear());
const month = ref(currentDate.getMonth() + 1);
const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

const currentMonth = computed(() => {
  const date = new Date(year.value, month.value);
  return `${date.getFullYear()}년 ${date.getMonth()}월`;
});

const calendar = computed(() => {
  const firstDayOfMonth = new Date(year.value, month.value - 1, 1).getDay();
  const daysInMonth = new Date(year.value, month.value, 0).getDate();

  const days = [];
  let day = 1;

  for (let i = 0; i < 6; i++) {
    const week = [];

    for (let j = 0; j < 7; j++) {
      if (i === 0 && j < firstDayOfMonth) {
        week.push({ date: '', isToday: false });
      } else if (day <= daysInMonth) {
        const currentDate = new Date(year.value, month.value - 1, day);
        const isToday =
            currentDate.toDateString() === new Date().toDateString();

        week.push({ date: day, isToday });
        day++;
      } else {
        week.push({ date: '', isToday: false });
      }
    }

    days.push(week);
    if (day > daysInMonth) break;
  }

  return days;
});


const previousMonth = () => {
  if (month.value === 1) {
    year.value--;
    month.value = 12;
  } else {
    month.value--;
  }
};

const nextMonth = () => {
  if (month.value === 12) {
    year.value++;
    month.value = 1;
  } else {
    month.value++;
  }
};

const selectedDate = ref(null);

const selectDate = (day) => {
  selectedDate.value = new Date(year.value, month.value, day.date);
};

const isSelected = (day) => {
  if (!selectedDate.value) {
    return false;
  }

  const selected = new Date(selectedDate.value);
  const currentDate = new Date(year.value, month.value, day.date);

  return selected.toDateString() === currentDate.toDateString();
};
</script>

<style scoped>
.today {
  background-color: lightblue;
}

.selected {
  background-color: yellow;
}
</style>
