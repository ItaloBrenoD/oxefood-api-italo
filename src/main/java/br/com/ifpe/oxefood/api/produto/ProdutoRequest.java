package br.com.ifpe.oxefood.api.produto;


import br.com.ifpe.oxefood.modelo.produto.Produto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {
      @Column
   private String codigo;

   @Column
   private String titulo;

   @Column
   private String descricao;

   @Column
   private double valorUnitario;

   @Column
   private int tempoEntregaMinimo;

   @Column
   private int tempoEntregaMaximo;

   public Produto build() {

       return Produto.builder()
           .codigo(codigo)
           .titulo(titulo)
           .descricao(descricao)
           .valorUnitario(valorUnitario)
           .tempoEntregaMinimo(tempoEntregaMinimo)
           .tempoEntregaMaximo(tempoEntregaMaximo)
           .build();
   }

}
