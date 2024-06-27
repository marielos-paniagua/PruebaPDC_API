package com.example.pruebapdc_api

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class DepartamentoFragment : Fragment() {
    private var lblBuscarDepto: EditText? = null
    private var btnBuscarDepto: ImageButton? = null
    private var lblPais: EditText? = null
    private var lblDepto: EditText? = null
    private var lblNombreDepto: EditText? = null
    private var btnGuardarDepto: Button? = null
    private var btnEditarDepto: Button? = null
    private var btnEliminarDepto: Button? = null

    private var lstPais: ListView? = null
    private var pais: Array<String> = emptyArray()
    private var nompais: Array<String> = emptyArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_departamento, container, false)

        val url = "http://192.168.0.6/PruebaPDC/consultapais.php"
        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    Log.i("ObtenerPais", "Response: $response")
                    val jsonArray = response.getJSONArray("data")
                    val paisList = ArrayList<String>()
                    val nompaisList = ArrayList<String>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val paisValue = jsonObject.getString("Pais")
                        val nompaisValue = jsonObject.getString("NomPais")
                        paisList.add(paisValue)
                        nompaisList.add(nompaisValue)
                        Log.i("ObtenerPais", "pais: $paisValue, nompais: $nompaisValue")
                    }
                    pais = paisList.toTypedArray()
                    nompais = nompaisList.toTypedArray()
                    Log.i("ObtenerPais", "pais initialized with ${pais.size} items")
                    Log.i("ObtenerPais", "nompais initialized with ${nompais.size} items")
                    val adapter = ArrayAdapter(root.context, android.R.layout.simple_list_item_1, nompais)
                    lstPais!!.adapter = adapter
                    lstPais!!.onItemClickListener =
                        OnItemClickListener { adapterView, view, i, l ->
                            lblPais!!.setText(pais[i])
                            lblDepto!!.setText("")
                        }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error al procesar la respuesta JSON", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Toast.makeText(context, "Error al consultar el país: $error", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)

        lblBuscarDepto = root.findViewById<View>(R.id.lblBuscarDepto) as EditText
        lblPais = root.findViewById<View>(R.id.lblPais) as EditText
        lblDepto = root.findViewById<View>(R.id.lblDepto) as EditText
        lblNombreDepto = root.findViewById<View>(R.id.lblNombreDepto) as EditText
        lstPais = root.findViewById<View>(R.id.lstPais) as ListView

        btnBuscarDepto = root.findViewById<View>(R.id.btnBuscarDepto) as ImageButton
        btnBuscarDepto!!.setOnClickListener {
            if (lblPais!!.length() != 0 && lblBuscarDepto!!.length() != 0) {
                ConsultaDepto()
            } else {
                Toast.makeText(context, "Complete los campos solicitados", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btnGuardarDepto = root.findViewById<View>(R.id.btnGuardarDepto) as Button
        btnGuardarDepto!!.setOnClickListener {
            if (lblPais!!.length() != 0 && lblDepto!!.length() != 0 && lblNombreDepto!!.length() != 0) {
                GuardarDepto()
            } else {
                Toast.makeText(context, "Complete los campos solicitados", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btnEditarDepto = root.findViewById<View>(R.id.btnEditarDepto) as Button
        btnEditarDepto!!.setOnClickListener {
            if (lblNombreDepto!!.length() != 0) {
                EditarDepto()
            } else {
                Toast.makeText(context, "Complete el campo solicitado", Toast.LENGTH_SHORT).show()
            }
        }
        btnEliminarDepto = root.findViewById<View>(R.id.btnEliminarDepto) as Button
        btnEliminarDepto!!.setOnClickListener( { EliminarDepto() })

        return root
    }

    fun GuardarDepto(){
        val url="http://192.168.0.6/PruebaPDC/insertardepartamento.php"
        val queue= Volley.newRequestQueue(context);
        val pais = lblPais?.text.toString()
        val depto = lblDepto?.text.toString()
        val nomdepto = lblNombreDepto?.text.toString()
        var resultadoPost = object : StringRequest(
            Request.Method.POST,url,
            Response.Listener{
                response -> Toast.makeText(context,"Departamento insertado exitosamente",Toast.LENGTH_LONG).show() }
            ,
            Response.ErrorListener { error -> Toast.makeText(context,"Error al guardar el departamento $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("pais",pais)
                parametros.put("depto",depto)
                parametros.put("nomdepto",nomdepto)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun ConsultaDepto(){
        val url="http://192.168.0.6/PruebaPDC/registrodepartamento.php?pais=${lblPais?.text.toString()}&depto=${lblBuscarDepto?.text.toString()}"
        val queue= Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener { response ->
                lblPais?.setText(response.getString("Pais"))
                lblDepto?.setText(response.getString("Depto"))
                lblNombreDepto?.setText(response.getString("NomDepto"))
                lblDepto!!.isEnabled = false
                btnGuardarDepto!!.isEnabled = false
                btnEditarDepto!!.isEnabled = true
                btnEliminarDepto!!.isEnabled = true
            }, Response.ErrorListener { error ->
                LimpiarCampos()
                Toast.makeText(context,"Error al consultar el departamento $error",Toast.LENGTH_LONG).show() })
        lblBuscarDepto!!.setText("")
        queue.add(jsonObjectRequest)
    }

    fun EliminarDepto(){
        val url="http://192.168.0.6/PruebaPDC/eliminardepartamento.php"
        val queue= Volley.newRequestQueue(context);
        val pais = lblPais?.text.toString()
        val depto = lblDepto?.text.toString()
        var resultadoPost = object : StringRequest(
            Request.Method.POST,url,
            Response.Listener{
                response -> Toast.makeText(context,"Departamento eliminado exitosamente",Toast.LENGTH_LONG).show() }
            ,
            Response.ErrorListener { error -> Toast.makeText(context,"Error al eliminar el departamento $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("pais",pais)
                parametros.put("depto",depto)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun EditarDepto(){
        val url="http://192.168.0.6/PruebaPDC/editardepartamento.php"
        val queue= Volley.newRequestQueue(context);
        val pais = lblPais?.text.toString()
        val depto = lblDepto?.text.toString()
        val nomdepto = lblNombreDepto?.text.toString()
        var resultadoPost = object : StringRequest(
            Request.Method.POST,url,
            Response.Listener{
                response -> Toast.makeText(context,"Departamento editado exitosamente",Toast.LENGTH_LONG).show() }
            ,
            Response.ErrorListener { error -> Toast.makeText(context,"Error al editar el departamento $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("pais",pais)
                parametros.put("depto",depto)
                parametros.put("nomdepto",nomdepto)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun LimpiarCampos() {
        lblPais!!.setText("")
        lblDepto!!.setText("")
        lblDepto!!.isEnabled = true
        lblNombreDepto!!.setText("")
        btnGuardarDepto!!.isEnabled = true
        btnEditarDepto!!.isEnabled = false
        btnEliminarDepto!!.isEnabled = false
    }

}