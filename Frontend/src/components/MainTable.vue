<script setup>
import {onMounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import {VueGoodTable} from 'vue-good-table-next'

const router = useRouter()
const rows = ref([])
const columns = ref([])
const API_URL = '/api/gastroset'

function boolFilterOptions() {
  return {
    placeholder: 'Все',
    enabled: true,
    filterDropdownItems: ['Да', 'Нет'],
    filterFn: (rowValue, filterText) => {
      if (!filterText) return true
      return (
          (filterText === 'Да' && rowValue === true) ||
          (filterText === 'Нет' && rowValue === false)
      )
    },
  }
}

function booleanColumn(label, field) {
  return {
    label,
    field,
    sortable: false,
    tdClass: 'bool-cell',
    filterOptions: boolFilterOptions(),
  }
}

columns.value = [
  {label: 'Фото', field: 'imageLink', sortable: false},
  {label: 'Вес', field: 'weight', sortable: true, type: 'number'},
  booleanColumn('На вынос', 'eatOutside'),
  booleanColumn('Бронь', 'bookingPossibility'),
  {label: 'Гастрофест', field: 'gastrofest', sortable: true},
  {label: 'Заведение', field: 'participant', sortable: true},
  booleanColumn('Ресторан', 'restaurant'),
]

async function loadData() {
  const cached = sessionStorage.getItem('gastroSets')
  if (cached) {
    rows.value = JSON.parse(cached)
    return
  }

  try {
    const response = await fetch(API_URL)
    rows.value = await response.json()
    sessionStorage.setItem('gastroSets', JSON.stringify(rows.value))
  } catch (e) {
    console.error('❌ Ошибка при загрузке gastroSets:', e)
  }
}

onMounted(async () => {
  await loadData();
  // После загрузки — добавляем фильтр
  const uniqueFests = [...new Set(rows.value.map(r => r.gastrofest))].filter(Boolean)
  const lastFest = uniqueFests.at(-1)
  const festColumn = columns.value.find(c => c.field === 'gastrofest')
  festColumn.filterOptions = {
    enabled: true,
    placeholder: 'Все',
    filterDropdownItems: uniqueFests,
    filterFn: (rowValue, filterText) => {
      if (!filterText) return true
      return rowValue === filterText
    },
    filterValue: lastFest
  }
})

function goToMeals(row) {
  sessionStorage.setItem('lastGastroSet', JSON.stringify(row))
  router.push({path: `/meals/${row.id}`})
}
</script>

<template>
  <div role="page-container">
    <div class="table-wrapper">
      <VueGoodTable
          :columns="columns"
          :rows="rows"
          style-class="vgt-table striped bordered"
      >
        <template #table-row="props">
          <!-- 🖼 Фото -->
          <span v-if="props.column.field === 'imageLink'">
            <img
                :src="props.row.imageLink"
                class="thumb"
                @click="goToMeals(props.row)"
                title="Посмотреть блюда"
            />
          </span>

          <!-- ✅❌ -->
          <span
              v-else-if="['eatOutside', 'bookingPossibility', 'restaurant'].includes(props.column.field)"
          >
            <span v-if="props.row[props.column.field]" class="text-green-500 text-lg">✅</span>
            <span v-else class="text-red-400 text-lg">❌</span>
          </span>

          <!-- 🔗 Заведение -->
          <span v-else-if="props.column.field === 'participant'">
            <a
                :href="props.row.url"
                target="_blank"
                rel="noopener noreferrer"
                class="text-blue-400 hover:underline"
            >
              <div v-for="line in props.row.participant.split('\n')" :key="line">
                {{ line }}
              </div>
            </a>
          </span>

          <!-- 📄 Остальные -->
          <span v-else>
            {{ props.formattedRow[props.column.field] }}
          </span>
        </template>
      </VueGoodTable>
    </div>
  </div>
</template>

<style scoped>
/* === Общий фон страницы === */
div[role="page-container"] {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  min-height: 100vh;
  background-color: #1e1e1e;
  color: #f0f0f0;
  padding: 32px 0;
}

/* === Контейнер таблицы === */
.table-wrapper {
  background-color: #252525;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 0 25px rgba(0, 0, 0, 0.6);
  max-width: 1000px;
  width: 90%;
  overflow-x: auto;
}

/* === Таблица (фон и текст) === */
:deep(.vgt-table),
:deep(.vgt-wrap) {
  background-color: #2d2d2d !important;
  border-color: #444 !important;
  color: #f0f0f0 !important;
}

/* === Шапка таблицы === */
:deep(.vgt-table thead),
:deep(.vgt-table thead tr),
:deep(.vgt-table thead th) {
  background: #2d2d2d !important;
  color: #f0f0f0 !important;
  border-bottom: 1px solid #444 !important;
  text-align: center !important;
}

/* === Полосатость строк === */
:deep(.vgt-table tbody tr:nth-child(even)) {
  background-color: #3a3a3a !important;
}

:deep(.vgt-table tbody tr:nth-child(odd)) {
  background-color: #333 !important;
}

:deep(.vgt-table tbody tr:hover) {
  background-color: #444 !important;
}

/* === Фото === */
.thumb {
  width: 300px;
  border-radius: 8px;
  object-fit: cover;
  cursor: pointer;
  transition: transform 0.15s ease;
  display: block;
  margin: 0 auto;
}
.thumb:hover {
  transform: scale(1.05);
}

/* === Ссылки === */
a {
  color: #58a6ff;
}

a:hover {
  color: #79b8ff;
  text-decoration: underline;
}

/* === Дропдауны и фильтры === */
:deep(select),
:deep(input[type="text"]),
:deep(.vgt-select),
:deep(.vgt-dropdown) {
  background-color: #333 !important;
  color: #f0f0f0 !important;
  border: 1px solid #555 !important;
  border-radius: 6px;
}

:deep(select:focus),
:deep(input[type="text"]:focus) {
  outline: none;
  border-color: #888 !important;
  box-shadow: 0 0 5px rgba(255, 255, 255, 0.3);
}

/* === MealPage текст (фикс глобального цвета) === */
:global(body),
:global(#app),
:global(.meal-page),
:global(.meal-page *) {
  color: #f0f0f0 !important;
}

/* === Глобальный фон === */
:global(html),
:global(body) {
  margin: 0;
  padding: 0;
  background-color: #1e1e1e !important;
}
</style>
