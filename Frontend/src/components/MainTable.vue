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
  {label: 'Вес', field: 'weight', sortable: true},
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
    console.log('✅ Используем gastroSets из sessionStorage')
    return
  }

  try {
    const response = await fetch(API_URL)
    rows.value = await response.json()
    sessionStorage.setItem('gastroSets', JSON.stringify(rows.value))
    console.log('✅ Сохранили gastroSets в sessionStorage')
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
  <div class="max-w-6xl mx-auto bg-white shadow rounded-2xl p-6">
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
          <span v-if="props.row[props.column.field]" class="text-green-600 text-lg">✅</span>
          <span v-else class="text-red-500 text-lg">❌</span>
        </span>

        <!-- 🔗 Заведение -->
        <span v-else-if="props.column.field === 'participant'">
          <a
              :href="props.row.url"
              target="_blank"
              rel="noopener noreferrer"
              class="text-blue-600 hover:underline"
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
</template>

<style scoped>
:deep(.vgt-table th),
:deep(.vgt-table td) {
  text-align: center;
  vertical-align: middle;
}

.thumb {
  width: 300px;
  border-radius: 8px;
  object-fit: cover;
  cursor: pointer;
  transition: transform 0.15s ease;
}
.thumb:hover {
  transform: scale(1.05);
}
</style>
