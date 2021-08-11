package org.skyfaced.kpm_test.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import org.skyfaced.kpm_test.utils.CABINET_MICRO_SERVICE_WSDL_URL

class CabinetMicroServiceImpl : CabinetMicroServiceApi {
    companion object {
        private const val NAMESPACE = "http://tempuri.org/"
    }

    override suspend fun getCCPromo(language: String) = withContext(Dispatchers.IO) {
        val methodName = "GetCCPromo"
        val soapAction = "http://tempuri.org/CabinetMicroService/GetCCPromo"

        val request = SoapObject(NAMESPACE, methodName)
        request.addProperty("lang", language)

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)

        val httpTransportSE = HttpTransportSE(CABINET_MICRO_SERVICE_WSDL_URL)

        httpTransportSE.call(soapAction, envelope)

        envelope.response.toString()
    }
}