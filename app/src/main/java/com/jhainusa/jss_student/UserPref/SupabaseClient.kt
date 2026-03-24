package com.jhainusa.jss_student.UserPref

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://lawyxhtjelxjstyygxij.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imxhd3l4aHRqZWx4anN0eXlneGlqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI3MDE5MDQsImV4cCI6MjA4ODI3NzkwNH0.gf8bvo6ZXX5MQSKHfzR1O_Dd_tce9Zi7EpgLef5NNCs"
) {
    install(Postgrest)
}