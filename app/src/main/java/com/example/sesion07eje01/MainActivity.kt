package com.example.sesion07eje01

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sesion07eje01.databinding.ActivityMainBinding
import java.io.File

/**
 * Ejercicio 29 — Almacenamiento externo con Scoped Storage (Android 10+)
 * Ruta: /storage/emulated/0/Android/data/<paquete>/files/archivo_externo.txt
 * No requiere permisos en API 29+.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val FILE_NAME = "archivo_externo.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener { guardarExterno() }
        binding.btnLoad.setOnClickListener { cargarExterno() }
    }

    private fun guardarExterno() {
        val texto = binding.editText.text.toString()

        if (texto.isBlank()) {
            toast("El campo está vacío")
            return
        }

        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            toast("Almacenamiento externo no disponible")
            return
        }

        try {
            // getExternalFilesDir(null) → directorio privado de la app en almacenamiento externo
            // No requiere permisos en Android 10+ (Scoped Storage)
            val dir = getExternalFilesDir(null)
                ?: throw IllegalStateException("Directorio externo no disponible")

            val archivo = File(dir, FILE_NAME)
            archivo.writeText(texto)   // Extensión Kotlin: escribe el String directamente

            toast("Guardado en: ${archivo.absolutePath}")
            binding.editText.setText("")
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Error al guardar: ${e.message}")
        }
    }

    private fun cargarExterno() {
        try {
            val dir = getExternalFilesDir(null)
                ?: throw IllegalStateException("Directorio externo no disponible")

            val archivo = File(dir, FILE_NAME)

            if (!archivo.exists()) {
                toast("El archivo no existe aún")
                return
            }

            val contenido = archivo.readText()   // Extensión Kotlin: lee todo el contenido
            binding.editText.setText(contenido)
            toast("Archivo cargado exitosamente")
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Error al cargar: ${e.message}")
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
