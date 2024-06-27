package com.example.pruebapdc_api

import android.content.Context
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

class PersonaFragment : Fragment() {
    private var lblBuscarPersona: EditText? = null
    private var btnBuscarPersona: ImageButton? = null
    private var lblPais: EditText? = null
    private var lblDepto: EditText? = null
    private var lblPersona: EditText? = null
    private var lblNombrePersona: EditText? = null
    private var lblDirecPersona: EditText? = null
    private var btnGuardarPersona: Button? = null
    private var btnEditarPersona: Button? = null
    private var btnEliminarPersona: Button? = null

    private var lstPais: ListView? = null
    private var pais: Array<String> = emptyArray()
    private var nompais: Array<String> = emptyArray()
    private var depto: Array<String> = emptyArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_persona, container, false)

        val url = "http://192.168.0.6/PruebaPDC/consultadepartamento.php"
        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    Log.i("ObtenerPais", "Response: $response")
                    val jsonArray = response.getJSONArray("data")
                    val paisList = ArrayList<String>()
                    val deptoList = ArrayList<String>()
                    val nompaisList = ArrayList<String>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val paisValue = jsonObject.getString("Pais")
                        val deptoValue = jsonObject.getString("Depto")
                        val nompaisValue = jsonObject.getString("NomPais")+"-"+jsonObject.getString("NomDepto")
                        paisList.add(paisValue)
                        deptoList.add(deptoValue)
                        nompaisList.add(nompaisValue)
                        Log.i("ObtenerPais", "pais: $paisValue, depto: $deptoValue, nompais: $nompaisValue")
                    }
                    pais = paisList.toTypedArray()
                    depto = deptoList.toTypedArray()
                    nompais = nompaisList.toTypedArray()
                    Log.i("ObtenerPais", "pais initialized with ${pais.size} items")
                    Log.i("ObtenerPais", "nompais initialized with ${nompais.size} items")
                    val adapter = ArrayAdapter(root.context, android.R.layout.simple_list_item_1, nompais)
                    lstPais!!.adapter = adapter
                    lstPais!!.onItemClickListener =
                        OnItemClickListener { adapterView, view, i, l ->
                            lblPais!!.setText(pais[i])
                            lblDepto!!.setText(depto[i])
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

        lblBuscarPersona = root.findViewById<View>(R.id.lblBuscarPersona) as EditText
        lblPais = root.findViewById<View>(R.id.lblPais) as EditText
        lblDepto = root.findViewById<View>(R.id.lblDepto) as EditText
        lblPersona = root.findViewById<View>(R.id.lblPersona) as EditText
        lblNombrePersona = root.findViewById<View>(R.id.lblNombrePersona) as EditText
        lblDirecPersona = root.findViewById<View>(R.id.lblDirecPersona) as EditText
        lstPais = root.findViewById<View>(R.id.lstPais) as ListView

        btnBuscarPersona = root.findViewById<View>(R.id.btnBuscarPersona) as ImageButton
        btnBuscarPersona!!.setOnClickListener {
            if (lblBuscarPersona!!.length() != 0) {
                ConsultaPersona()
            } else {
                Toast.makeText(context, "Complete los campos solicitados", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btnGuardarPersona = root.findViewById<View>(R.id.btnGuardarPersona) as Button
        btnGuardarPersona!!.setOnClickListener {
            if (lblPais!!.length() != 0 && lblDepto!!.length() != 0 && lblNombrePersona!!.length() != 0) {
                GuardarPersona()
            } else {
                Toast.makeText(context, "Complete los campos solicitados", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btnEditarPersona = root.findViewById<View>(R.id.btnEditarPersona) as Button
        btnEditarPersona!!.setOnClickListener {
            if (lblNombrePersona!!.length() != 0 && lblPais!!.length() != 0 && lblDepto!!.length() != 0) {
                EditarPersona()
            } else {
                Toast.makeText(context, "Complete el campo solicitado", Toast.LENGTH_SHORT).show()
            }
        }
        btnEliminarPersona = root.findViewById<View>(R.id.btnEliminarPersona) as Button
        btnEliminarPersona!!.setOnClickListener(View.OnClickListener { EliminarPersona() })

        return root
    }

    fun GuardarPersona(){
        val url="http://192.168.0.6/PruebaPDC/insertarpersona.php"
        val queue= Volley.newRequestQueue(context);
        val idpersona = lblPersona?.text.toString()
        val nombre = lblNombrePersona?.text.toString()
        val pais = lblPais?.text.toString()
        val depto = lblDepto?.text.toString()
        val direccion = lblDirecPersona?.text.toString()
        var resultadoPost = object : StringRequest(
            Request.Method.POST,url,
            Response.Listener{
                    response -> Toast.makeText(context,"Persona insertada exitosamente",Toast.LENGTH_LONG).show() }
            ,
            Response.ErrorListener { error -> Toast.makeText(context,"Error al guardar a la persona $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("idpersona",idpersona)
                parametros.put("nombre",nombre)
                parametros.put("pais",pais)
                parametros.put("departamento",depto)
                parametros.put("direccion",direccion)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun ConsultaPersona(){
        val url="http://192.168.0.6/PruebaPDC/registropersona.php?idpersona=${lblBuscarPersona?.text.toString()}"
        val queue= Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener { response ->
                lblPersona?.setText(response.getString("IdPersona"))
                lblNombrePersona?.setText(response.getString("Nombre"))
                lblPais?.setText(response.getString("Pais"))
                lblDepto?.setText(response.getString("Departamento"))
                lblDirecPersona?.setText(response.getString("Direccion"))
                lblPersona!!.isEnabled = false
                btnGuardarPersona!!.isEnabled = false
                btnEditarPersona!!.isEnabled = true
                btnEliminarPersona!!.isEnabled = true
            }, Response.ErrorListener { error ->
                LimpiarCampos()
                Toast.makeText(context,"Error al consultar a la persona $error",Toast.LENGTH_LONG).show() })
        lblBuscarPersona!!.setText("")
        queue.add(jsonObjectRequest)
    }

    fun EliminarPersona(){
        val url="http://192.168.0.6/PruebaPDC/eliminarpersona.php"
        val queue= Volley.newRequestQueue(context);
        val idpersona = lblPersona?.text.toString()
        var resultadoPost = object : StringRequest(
            Request.Method.POST,url,
            Response.Listener{
                    response -> Toast.makeText(context,"Persona eliminada exitosamente",Toast.LENGTH_LONG).show() }
            ,
            Response.ErrorListener { error -> Toast.makeText(context,"Error al eliminar a la persona $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("idpersona",idpersona)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun EditarPersona(){
        val url="http://192.168.0.6/PruebaPDC/editarpersona.php"
        val queue= Volley.newRequestQueue(context);
        val idpersona = lblPersona?.text.toString()
        val nombre = lblNombrePersona?.text.toString()
        val pais = lblPais?.text.toString()
        val depto = lblDepto?.text.toString()
        val direccion = lblDirecPersona?.text.toString()
        var resultadoPost = object : StringRequest(
            Request.Method.POST,url,
            Response.Listener{
                    response -> Toast.makeText(context,"Persona editada exitosamente",Toast.LENGTH_LONG).show() }
            ,
            Response.ErrorListener { error -> Toast.makeText(context,"Error al editar a la persona $error",Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("idpersona",idpersona)
                parametros.put("nombre",nombre)
                parametros.put("pais",pais)
                parametros.put("departamento",depto)
                parametros.put("direccion",direccion)
                return parametros
            }
        }
        queue.add(resultadoPost)
        LimpiarCampos()
    }

    fun LimpiarCampos() {
        lblPersona!!.setText("")
        lblNombrePersona!!.setText("")
        lblPais!!.setText("")
        lblDepto!!.setText("")
        lblDirecPersona!!.setText("")
        lblPersona!!.isEnabled = true
        lblNombrePersona!!.setText("")
        btnGuardarPersona!!.isEnabled = true
        btnEditarPersona!!.isEnabled = false
        btnEliminarPersona!!.isEnabled = false
    }

}