package com.example.pruebapdc_api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class PaisFragment : Fragment() {

    private var lblBuscarPais: EditText? = null
    private var btnBuscarPais: ImageButton? = null
    private var lblPais: EditText? = null
    private var lblNombrePais: EditText? = null
    private var btnGuardarPais: Button? = null
    private var btnEditarPais: Button? = null
    private var btnEliminarPais: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_pais, container, false)
        lblBuscarPais = root.findViewById<View>(R.id.lblBuscarPais) as EditText
        lblPais = root.findViewById<View>(R.id.lblDepto) as EditText
        lblNombrePais = root.findViewById<View>(R.id.lblNombreDepto) as EditText


        btnBuscarPais = root.findViewById<View>(R.id.btnBuscarPais) as ImageButton
        btnBuscarPais!!.setOnClickListener {
            ConsultaPais()
        }
        btnGuardarPais = root.findViewById<View>(R.id.btnGuardarDepto) as Button
        btnGuardarPais!!.setOnClickListener {
            if (lblPais!!.length() != 0 && lblNombrePais!!.length() != 0) {
                GuardarPais()
            } else {
                Toast.makeText(context, "Complete los campos solicitados", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btnEditarPais = root.findViewById<View>(R.id.btnEditarDepto) as Button
        btnEditarPais!!.setOnClickListener {
            if (lblNombrePais!!.length() != 0) {
                EditarPais()
            } else {
                Toast.makeText(context, "Complete el campo solicitado", Toast.LENGTH_SHORT).show()
            }
        }
        btnEliminarPais = root.findViewById<View>(R.id.btnEliminarDepto) as Button
        btnEliminarPais!!.setOnClickListener {
            EliminarPais()
        }

        return root
    }

    fun GuardarPais(){
        val url="http://192.168.0.6/PruebaPDC/insertarpais.php"
        val queue= Volley.newRequestQueue(context);
        val pais = lblPais?.text.toString()
        val nompais = lblNombrePais?.text.toString()
        var resultadoPost = object : StringRequest(Request.Method.POST,url,Response.Listener{
            response -> Toast.makeText(context,"País insertado exitosamente",Toast.LENGTH_LONG).show() }
            ,Response.ErrorListener { error -> Toast.makeText(context,"Error al guardar el país $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("pais",pais)
                parametros.put("nompais",nompais)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun ConsultaPais(){
        val url="http://192.168.0.6/PruebaPDC/registropais.php?pais=${lblBuscarPais?.text.toString()}"
        val queue=Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener { response ->
                lblPais?.setText(response.getString("Pais"))
                lblNombrePais?.setText(response.getString("NomPais"))
                lblPais!!.isEnabled = false
                btnGuardarPais!!.isEnabled = false
                btnEditarPais!!.isEnabled = true
                btnEliminarPais!!.isEnabled = true
            },Response.ErrorListener { error ->
                LimpiarCampos()
                Toast.makeText(context,"Error al consultar el país $error",Toast.LENGTH_LONG).show() })
        lblBuscarPais!!.setText("")
        queue.add(jsonObjectRequest)
    }

    fun EliminarPais(){
        val url="http://192.168.0.6/PruebaPDC/eliminarpais.php"
        val queue= Volley.newRequestQueue(context);
        val pais = lblPais?.text.toString()
        var resultadoPost = object : StringRequest(Request.Method.POST,url,Response.Listener{
            response -> Toast.makeText(context,"País eliminado exitosamente",Toast.LENGTH_LONG).show() }
            ,Response.ErrorListener { error -> Toast.makeText(context,"Error al eliminar el país $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("pais",pais)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun EditarPais(){
        val url="http://192.168.0.6/PruebaPDC/editarpais.php"
        val queue= Volley.newRequestQueue(context);
        val pais = lblPais?.text.toString()
        val nompais = lblNombrePais?.text.toString()
        var resultadoPost = object : StringRequest(Request.Method.POST,url,Response.Listener{
            response -> Toast.makeText(context,"País editado exitosamente",Toast.LENGTH_LONG).show() }
            ,Response.ErrorListener { error -> Toast.makeText(context,"Error al editar el país $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("pais",pais)
                parametros.put("nompais",nompais)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun LimpiarCampos() {
        lblPais!!.setText("")
        lblPais!!.isEnabled = true
        lblNombrePais!!.setText("")
        btnGuardarPais!!.isEnabled = true
        btnEditarPais!!.isEnabled = false
        btnEliminarPais!!.isEnabled = false
    }
}