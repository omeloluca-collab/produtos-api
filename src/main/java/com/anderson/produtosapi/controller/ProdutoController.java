package com.anderson.produtosapi.controller;

import com.anderson.produtosapi.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private static final List<Produto> produtos = new ArrayList<>();
    private static final AtomicInteger contadorId = new AtomicInteger(1);

    static {
        produtos.add(new Produto(contadorId.getAndIncrement(), "Notebook", 3500.0));
        produtos.add(new Produto(contadorId.getAndIncrement(), "Mouse", 120.0));
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return produtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {
        Optional<Produto> produto = produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return produto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Produto> adicionar(@RequestBody Produto novoProduto) {
        novoProduto.setId(contadorId.getAndIncrement());
        produtos.add(novoProduto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Integer id, @RequestBody Produto dadosAtualizados) {
        Optional<Produto> produtoOpt = produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (produtoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Produto produto = produtoOpt.get();

        if (dadosAtualizados.getNome() != null) {
            produto.setNome(dadosAtualizados.getNome());
        }

        if (dadosAtualizados.getPreco() != null) {
            produto.setPreco(dadosAtualizados.getPreco());
        }

        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        boolean removido = produtos.removeIf(p -> p.getId().equals(id));

        if (!removido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}