<script setup>
import {onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'

const route = useRoute()
const router = useRouter()
const gastroSet = ref(null)

console.log('✅ MealPage mounted — route.params:', route.params)
console.log('✅ MealPage mounted — history.state:', history.state)

if (history.state?.gastroSet) {
  gastroSet.value = history.state.gastroSet
  console.log('✅ Используем gastroSet из history.state')
} else {
  const cached = sessionStorage.getItem('lastGastroSet')
  if (cached) {
    gastroSet.value = JSON.parse(cached)
    console.log('✅ Используем gastroSet из sessionStorage')
  }
}

onMounted(async () => {
  if (!gastroSet.value) {
    console.warn('⚠️ gastroSet не найден — идём в бэк...')
    const id = route.params.id
    try {
      const response = await fetch(`/api/gastroset/${id}`)
      gastroSet.value = await response.json()
      console.log('✅ Ответ от бэка:', gastroSet.value)
    } catch (e) {
      console.error('❌ Ошибка при загрузке из бэка:', e)
    }
  } else {
    // кешируем для возврата назад
    sessionStorage.setItem('lastGastroSet', JSON.stringify(gastroSet.value))
  }
})

function goBack() {
  if (window.history.length > 1) router.back()
  else router.push('/')
}
</script>

<template>
  <div
      v-if="gastroSet"
      class="min-h-screen flex flex-col items-center justify-start text-center px-6 py-10 space-y-8"
  >
    <!-- 🔹 Кнопка Назад -->
    <div class="w-full flex justify-start mb-6 max-w-5xl">
      <button
          @click="goBack"
          class="px-4 py-2 bg-blue-500 text-white rounded-xl hover:bg-blue-600 transition"
      >
        ⬅ Назад
      </button>
    </div>

    <!-- 🔹 Название участника -->
    <h1 class="text-3xl font-bold mb-6">{{ gastroSet.participant }}</h1>

    <!-- 🔹 Описания блюд -->
    <div
        v-if="gastroSet.mealsDescriptions?.length"
        class="max-w-3xl text-left space-y-4 mb-10"
    >
      <div
          v-for="(desc, i) in gastroSet.mealsDescriptions"
          :key="'desc-' + i"
          class="bg-gray-50 rounded-2xl p-4 shadow-sm"
      >
        <p class="text-gray-800 text-base leading-relaxed">{{ desc }}</p>
      </div>
    </div>

    <!-- 🔹 Картинки блюд -->
    <div
        v-if="gastroSet.mealsImages?.length"
        class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8 justify-center items-start max-w-5xl w-full"
    >
      <img
          v-for="(img, i) in gastroSet.mealsImages"
          :key="i"
          :src="img"
          class="rounded-2xl w-full object-cover shadow-lg"
          alt=""
      />
    </div>
  </div>

  <div v-else class="text-center py-12 text-gray-500">
    Загрузка данных...
  </div>
</template>
