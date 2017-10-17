package contracts.dict

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        name "shouldReturnDict"
        method GET()
        url '/api/v1/dicts/1'
    }
    response {
        status 200
        body """
            {"id":1,"code":"gender","name":"性别","rank":1,"parentId":0}
        """
        headers {
            contentType("application/json")
        }
    }
}