package org.example

import java.io.BufferedReader
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/*
El fichero cotizacion.csv (que podéis encontrar en la carpeta ficheros) contiene
las cotizaciones de las empresas del IBEX35 con las siguientes columnas:
Nombre (nombre de la empresa),
Final (precio de la acción al cierre de bolsa),
Máximo (precio máximo de la acción durante la jornada),
Mínimo (precio mínimo de la acción durante la jornada),
Volumen (Volumen al cierre de bolsa),
Efectivo (capitalización al cierre en miles de euros).

Construir una función reciba el fichero de cotizaciones y devuelva un diccionario
con los datos del fichero por columnas.

Construir una función que reciba el diccionario devuelto por la función anterior
y cree un fichero en formato csv con el mínimo, el máximo y la media de dada columna.
*/

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val ficheroEntrada = Paths.get("src/ficheros/cotizacion.csv")
    val ficheroSalida = Paths.get("src/ficheros/estadisticas_cotizaciones.csv")

    println("Leyendo fichero de cotizaciones...")
    val cotizaciones = leerFichero(ficheroEntrada)

    cotizaciones.forEach { (columna, valores) ->
        println("$columna: $valores")
    }

    println("Escribiendo estadísticas en el fichero...")
    escribirEstadisticas(cotizaciones, ficheroSalida)

    println("Proceso completado. Las estadísticas han sido generadas correctamente.")
}


fun leerFichero(ruta: Path): MutableMap<String, List<String>> {
    val diccionario: MutableMap<String, List<String>> = mutableMapOf()
    val br: BufferedReader = Files.newBufferedReader(ruta)

    val lineasArchivo = mutableListOf<List<String>>()

    br.use {
        it.forEachLine { line ->


            // Formateamos la línea borramos los puntos de los números si es necesario
            val lineaSpliteada: List<String> = line.split(";")
            lineasArchivo.add(lineaSpliteada)
            diccionario[lineaSpliteada[0]] = listOf(
                lineaSpliteada[1],
                lineaSpliteada[2],
                lineaSpliteada[3],
                lineaSpliteada[4],
                lineaSpliteada[5]
            )

        }
    }
    return diccionario
}


fun escribirEstadisticas(diccionario: MutableMap<String, List<String>>, rutaSalida: Path) {
    val bw: BufferedWriter =
        Files.newBufferedWriter(rutaSalida, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)

    bw.use {

        diccionario.forEach { (columna, valores) ->

            val max = valores[1].replace(".", "").replace(",", ".")
            val min = valores[2].replace(".", "").replace(",", ".")

            if (max.toFloatOrNull() != null) {
                it.write("$columna;$max;$min;${((min.toFloat() + max.toFloat()) / 2)}\n")

            } else {
                it.write("${columna};${valores[1]};${valores[2]};Media\n")
            }

        }
    }
}