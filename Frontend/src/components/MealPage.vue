<script setup>
import {onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'

const route = useRoute()
const router = useRouter()
const gastroSet = ref(null)

const cached = sessionStorage.getItem('lastGastroSet')
if (cached) {
  gastroSet.value = JSON.parse(cached)
}

onMounted(async () => {
  if (!gastroSet.value) {
    const id = route.params.id
    try {
      const response = await fetch(`/api/gastroset/${id}`)
      gastroSet.value = await response.json()
    } catch (e) {
      console.error('❌ Ошибка при загрузке из бэка:', e)
    }
  } else {
    sessionStorage.setItem('lastGastroSet', JSON.stringify(gastroSet.value))
  }
})

function goBack() {
  if (window.history.length > 1) router.back()
  else router.push('/')
}
</script>

<template>
  <div class="w-full max-w-5xl px-4 mb-6 flex justify-start">
    <button
        @click="goBack"
        class="px-5 py-2 bg-blue-500 text-white rounded-xl hover:bg-blue-600 transition"
    >
      ⬅ Назад
    </button>
  </div>
  <div role="page-container">
    <div v-if="gastroSet" class="min-h-screen flex flex-col items-center justify-start py-8 bg-gray-50">
      <!-- Основной контент -->
      <div class="max-w-3xl w-full mx-auto text-center flex flex-col items-center space-y-8 px-4">
        <!-- Заголовок -->
        <div class="mb-4">
          <h1
              v-for="line in gastroSet.participant?.split('\n')"
              :key="line"
              class="text-3xl font-bold text-gray-900"
          >
            {{ line }}
          </h1>
        </div>
        <h1 v-if="gastroSet.theme" class="text-3xl font-bold mb-4">{{ "«" + gastroSet.theme + "»" }}</h1>

        <!-- Описания блюд -->
        <div
            v-if="gastroSet.mealsDescriptions?.length"
            class="flex flex-col items-center space-y-4 w-full max-w-2xl"
        >
          <p
              v-for="(desc, i) in gastroSet.mealsDescriptions"
              :key="'desc-' + i"
              class="text-gray-700 text-lg text-center leading-relaxed"
          >
            {{ desc }}
          </p>
        </div>

        <!-- Изображения -->
        <div
            v-if="gastroSet.mealsImages?.length"
            class="flex flex-wrap justify-center gap-6 mt-4"
        >
          <img
              v-for="(img, i) in gastroSet.mealsImages"
              :key="'img-' + i"
              :src="img"
              class="rounded-2xl shadow-md object-cover max-w-xs w-full"
              alt="meal"
          />
        </div>
      </div>
    </div>

    <div v-else class="text-center py-12 text-gray-500">
      Загрузка данных...
    </div>
  </div>
</template>

<style scoped>
div[role="page-container"] {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  justify-content: flex-start;
  width: 100%;
}

div[role="page-container"] * {
  text-align: center !important;
  margin-left: auto;
  margin-right: auto;
}
</style>
