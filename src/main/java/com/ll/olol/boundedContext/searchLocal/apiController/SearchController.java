package com.ll.olol.boundedContext.searchLocal.apiController;

import com.ll.olol.boundedContext.searchLocal.NaverSearchClient;
import com.ll.olol.boundedContext.searchLocal.SearchLocalReq;
import com.ll.olol.boundedContext.searchLocal.SearchLocalRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/server")
public class SearchController {
    private final NaverSearchClient naverSearchClient;

    public SearchController(NaverSearchClient naverSearchClient) {
        this.naverSearchClient = naverSearchClient;
    }

    @PostMapping("/search")
    public List<String> search(@RequestParam String query) {
        SearchLocalReq searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);

        SearchLocalRes searchLocalRes = naverSearchClient.searchLocal(searchLocalReq);

        List<String> addresses = new ArrayList<>();
        if (searchLocalRes != null && searchLocalRes.getItems() != null) {
            for (SearchLocalRes.Item item : searchLocalRes.getItems()) {
                addresses.add(item.getAddress());
            }
        }

        return addresses;
    }
}
